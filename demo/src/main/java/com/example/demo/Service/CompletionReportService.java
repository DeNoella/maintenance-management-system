package com.example.mms.service;

import com.example.mms.dto.CompletionReportRequest;
import com.example.mms.dto.ReviewReportRequest;
import com.example.mms.model.*;
import com.example.mms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompletionReportService {

    private final CompletionReportRepository reportRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    // US-T5: Technician submits a completion report
    public CompletionReport submitReport(CompletionReportRequest req) {
        MaintenanceRequest request = requestRepository.findById(req.getRequestId())
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));

        User technician = userRepository.findById(req.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        // Only the assigned technician can submit the report
        if (!request.getTechnician().getId().equals(req.getTechnicianId())) {
            throw new RuntimeException("You are not assigned to this maintenance request.");
        }

        CompletionReport report = CompletionReport.builder()
                .request(request)
                .technician(technician)
                .workSummary(req.getWorkSummary())
                .approvalStatus(ApprovalStatus.PENDING)
                .build();

        CompletionReport saved = reportRepository.save(report);

        // Update request status to IN_PROGRESS once report is submitted
        request.setStatus(RequestStatus.IN_PROGRESS);
        requestRepository.save(request);

        activityLogService.log(req.getTechnicianId(), "SUBMIT_COMPLETION_REPORT",
                "CompletionReport", saved.getId(),
                "Submitted completion report for request: " + req.getRequestId());
        return saved;
    }

    // US-BM5: Branch Manager approves or rejects the completion report
    public CompletionReport reviewReport(Long reportId, ReviewReportRequest req) {
        CompletionReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Completion report not found"));

        User manager = userRepository.findById(req.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        ApprovalStatus decision = ApprovalStatus.valueOf(req.getDecision());
        report.setApprovalStatus(decision);
        report.setApprovedBy(manager);
        report.setApprovedAt(LocalDateTime.now());

        CompletionReport saved = reportRepository.save(report);

        // If approved → mark maintenance request as COMPLETED (US-BM5)
        if (decision == ApprovalStatus.APPROVED) {
            MaintenanceRequest mainReq = report.getRequest();
            mainReq.setStatus(RequestStatus.COMPLETED);
            requestRepository.save(mainReq);
        }

        activityLogService.log(req.getManagerId(), "REVIEW_COMPLETION_REPORT",
                "CompletionReport", reportId, "Decision: " + decision);

        return saved;
    }

    // US-BM1: Branch Manager views reports scoped to their branch
    public List<CompletionReport> getByBranch(Long branchId) {
        return reportRepository.findByRequest_BranchId(branchId);
    }

    public List<CompletionReport> getByTechnician(Long techId) {
        return reportRepository.findByTechnicianId(techId);
    }

    public List<CompletionReport> getAll() {
        return reportRepository.findAll();
    }

    public CompletionReport getById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Completion report not found"));
    }
}
