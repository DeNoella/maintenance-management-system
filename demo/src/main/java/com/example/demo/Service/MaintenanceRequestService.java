package com.example.demo.Service;

import com.example.demo.dto.MaintenanceRequestDTO;
import com.example.demo.dto.UpdateStatusRequest;
import com.example.demo.Enum.*;
import com.example.demo.Enum.Role;
import com.example.demo.Model.AccessToken;
import com.example.demo.Model.Branch;
import com.example.demo.Model.MaintenanceRequest;
import com.example.demo.Model.User;
import com.example.demo.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceRequestService {

    private final MaintenanceRequestRepository requestRepository;
    private final AccessTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ActivityLogService activityLogService;

    // US-T2: Technician submits a maintenance request
    public MaintenanceRequest submitRequest(MaintenanceRequestDTO dto) {
        User technician = userRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        if (technician.getRole() != Role.TECHNICIAN) {
            throw new RuntimeException("Only technicians can submit maintenance requests.");
        }

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        MaintenanceRequest request = MaintenanceRequest.builder()
                .technician(technician)
                .branch(branch)
                .issueDescription(dto.getIssueDescription())
                .priority(Priority.valueOf(dto.getPriority()))
                .status(RequestStatus.PENDING)
                .preferredVisitDate(LocalDate.parse(dto.getPreferredVisitDate()))
                .build();

        MaintenanceRequest saved = requestRepository.save(request);
        activityLogService.log(dto.getTechnicianId(), "SUBMIT_REQUEST",
                saved.getId(), "MaintenanceRequest", "Submitted maintenance request for branch: " + branch.getName());
        return saved;
    }

    // US-BM1: Branch Manager approves or rejects request
    // When approved → auto-generate access token
    public MaintenanceRequest updateStatus(Long requestId, UpdateStatusRequest dto) {
        MaintenanceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        RequestStatus newStatus = RequestStatus.valueOf(dto.getStatus());
        request.setStatus(newStatus);
        MaintenanceRequest saved = requestRepository.save(request);

        // Auto-generate token when approved (US-T2 acceptance criteria)
        if (newStatus == RequestStatus.APPROVED) {
            generateAccessToken(saved);
        }

        activityLogService.log(dto.getManagerId(), "UPDATE_REQUEST_STATUS",
                requestId, "MaintenanceRequest", "Status changed to: " + newStatus);
        return saved;
    }

    // Internal: generates token automatically on approval
    private void generateAccessToken(MaintenanceRequest request) {
        String tokenCode = UUID.randomUUID().toString();
        AccessToken token = AccessToken.builder()
                .request(request)
                .technician(request.getTechnician())
                .branch(request.getBranch())
                .tokenCode(tokenCode)
                .qrData("MMS-TOKEN:" + tokenCode)
                .status(TokenStatus.ACTIVE)
                .validFrom(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        tokenRepository.save(token);
    }

    public List<MaintenanceRequest> getAll() {
        return requestRepository.findAll();
    }

    public List<MaintenanceRequest> getByTechnician(Long techId) {
        return requestRepository.findByTechnicianId(techId);
    }

    // US-BM1: Branch manager only sees their branch
    public List<MaintenanceRequest> getByBranch(Long branchId) {
        return requestRepository.findByBranchId(branchId);
    }

    public List<MaintenanceRequest> getByStatus(String status) {
        return requestRepository.findByStatus(RequestStatus.valueOf(status));
    }

    public MaintenanceRequest getById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }
}
