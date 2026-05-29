package com.example.demo.Controller;

import com.example.demo.dto.MaintenanceRequestDTO;
import com.example.demo.dto.UpdateStatusRequest;
import com.example.demo.Model.MaintenanceRequest;
import com.example.demo.Service.MaintenanceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class MaintenanceRequestController {

    private final MaintenanceRequestService requestService;

    /**
     * US-T2 — Technician submits a maintenance request
     * POST /api/requests
     * Role: TECHNICIAN only
     * Body: { technicianId, branchId, issueDescription, priority, preferredVisitDate }
     */
    @PostMapping
    public ResponseEntity<MaintenanceRequest> submitRequest(
            @RequestBody MaintenanceRequestDTO dto) {
        return ResponseEntity.ok(requestService.submitRequest(dto));
    }

    /**
     * US-BM1 — Branch Manager approves or rejects a request
     * PATCH /api/requests/{id}/status
     * Role: BRANCH_MANAGER only
     * Body: { status: "APPROVED" | "REJECTED", managerId }
     * Note: On APPROVED, an access token is auto-generated
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<MaintenanceRequest> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest dto) {
        return ResponseEntity.ok(requestService.updateStatus(id, dto));
    }

    /**
     * US-A6 — Admin gets all requests system-wide
     * GET /api/requests
     * Role: ADMIN
     */
    @GetMapping
    public ResponseEntity<List<MaintenanceRequest>> getAll() {
        return ResponseEntity.ok(requestService.getAll());
    }

    /**
     * US-T2 — Technician views their own submitted requests
     * GET /api/requests/technician/{techId}
     * Role: TECHNICIAN
     */
    @GetMapping("/technician/{techId}")
    public ResponseEntity<List<MaintenanceRequest>> getByTechnician(@PathVariable Long techId) {
        return ResponseEntity.ok(requestService.getByTechnician(techId));
    }

    /**
     * US-BM1 — Branch Manager views requests for their branch only
     * GET /api/requests/branch/{branchId}
     * Role: BRANCH_MANAGER
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<MaintenanceRequest>> getByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(requestService.getByBranch(branchId));
    }

    /**
     * Filter requests by status
     * GET /api/requests/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceRequest>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(requestService.getByStatus(status));
    }

    /**
     * Get a single request by ID
     * GET /api/requests/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getById(id));
    }
}
