package com.example.demo.Controller;

import com.example.mms.dto.CreateBranchRequest;
import com.example.mms.model.Branch;
import com.example.mms.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    /**
     * US-A4 — Admin creates a branch under a company
     * POST /api/branches?actorId={adminId}
     * Role: ADMIN only
     * Body: { name, address, companyId }
     */
    @PostMapping
    public ResponseEntity<Branch> createBranch(
          @RequestBody CreateBranchRequest request,
          @RequestParam Long actorId) {
        return ResponseEntity.ok(branchService.createBranch(request, actorId));
        }

    /**
     * US-A4 — Admin updates a branch
     * PUT /api/branches/{id}?actorId={adminId}
     * Role: ADMIN only
     */
    @PutMapping("{id}")
    public ResponseEntity<Branch> updateBranch(
        @PathVariable Long id,
        @RequestBody CreateBranchRequest request,
        @RequestParam Long actorId) {
            return responseEntity.ok(branchService.updateBranch(id, request, actorId));
        }
    /**
     * US-A4 — Admin deactivates a branch
     * PATCH /api/branches/{id}/deactivate?actorId={adminId}
     * Role: ADMIN only
     */
    @PatchMapping("/{id}/{deactivate}")
    public ResponseEntity<Branch> deactivateBranch(
            @PathVariable Long id,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(branchService.deactivateBranch(id, actorId));
        }
    /**
     * Get all branches (Admin view)
     * GET /api/branches
     */
    @GetMapping
    public ResponseEntity<List<Branch>> getAll() {
        return ResponseEntity.ok(branchService.getAll());
    }

    /**
     * Get a single branch by ID
     * GET /api/branches/{id}
     */
    @GetMapping("{/id}")
    public ResponseEntity<Branch> getById(
        @PathVariable Long id) {
            return ResponseEntity.ok(branchService.getById(id));
        }
    /**
     * Get all branches under a company
     * GET /api/branches/company/{companyId}
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Branch>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(branchService.getByCompany(companyId));
    }

}
