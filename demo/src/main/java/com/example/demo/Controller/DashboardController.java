package com.example.demo.Controller;

import com.example.demo.dto.DashboardStats;
import com.example.demo.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * US-A1 — Admin dashboard: system-wide summary
     * GET /api/dashboard/admin
     * Role: ADMIN only
     */
    @GetMapping("/admin")
    public ResponseEntity<DashboardStats> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    /**
     * US-BM1 — Branch Manager dashboard: scoped to their branch
     * GET /api/dashboard/manager/{branchId}
     * Role: BRANCH_MANAGER only
     */
    @GetMapping("/manager/{branchId}")
    public ResponseEntity<DashboardStats> getManagerDashboard(@PathVariable Long branchId) {
        return ResponseEntity.ok(dashboardService.getBranchManagerDashboard(branchId));
    }
}
