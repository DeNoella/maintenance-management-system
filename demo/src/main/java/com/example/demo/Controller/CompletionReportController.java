package com.example.demo.Controller;

import com.example.demo.dto.*;
import com.example.demo.Model.*;
import com.example.demo.Service.CompletionReportService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/completion-reports")
@RequiredArgsConstructor
public class CompletionReportController {

    private final CompletionReportService reportService;

    /**
     * US-T5 — Technician submits a completion report after finishing work
     * POST /api/completion-reports
     * Role: TECHNICIAN
     * Body: { requestId, technicianId, workSummary }
     */
    @PostMapping
    public ResponseEntity<CompletionReport> submitReport(
            @RequestBody CompletionReportRequest request) {
        return ResponseEntity.ok(reportService.submitReport(request));
    }

    /**
     * US-BM5 — Branch Manager approves or rejects a completion report
     * PATCH /api/completion-reports/{id}/review
     * Role: BRANCH_MANAGER
     * Body: { managerId, decision: "APPROVED" | "REJECTED" }
     * Note: On APPROVED, maintenance request is marked COMPLETED
     */
    @PatchMapping("/{id}/review")
    public ResponseEntity<CompletionReport> reviewReport(
            @PathVariable Long id,
            @RequestBody ReviewReportRequest request) {
        return ResponseEntity.ok(reportService.reviewReport(id, request));
    }

    /**
     * US-BM1 — Branch Manager views completion reports for their branch
     * GET /api/completion-reports/branch/{branchId}
     * Role: BRANCH_MANAGER
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CompletionReport>> getByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(reportService.getByBranch(branchId));
    }

    /**
     * Technician views their own completion reports
     * GET /api/completion-reports/technician/{techId}
     * Role: TECHNICIAN
     */
    @GetMapping("/technician/{techId}")
    public ResponseEntity<List<CompletionReport>> getByTechnician(@PathVariable Long techId) {
        return ResponseEntity.ok(reportService.getByTechnician(techId));
    }

    /**
     * Admin views all completion reports
     * GET /api/completion-reports
     * Role: ADMIN
     */
    @GetMapping
    public ResponseEntity<List<CompletionReport>> getAll() {
        return ResponseEntity.ok(reportService.getAll());
    }

    /**
     * Get a single report by ID
     * GET /api/completion-reports/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompletionReport> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getById(id));
    }
}
