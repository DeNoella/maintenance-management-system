package com.example.mms.controller;

import com.example.mms.dto.CreateUserRequest;
import com.example.mms.model.TechnicianSkill;
import com.example.mms.model.User;
import com.example.mms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * US-A2 — Admin creates a Branch Manager or Technician
     * POST /api/users/admin/create?actorId={adminId}
     * Role: ADMIN only
     * Body: { username, fullName, phone, role, branchId, password }
     */
    @PostMapping("/admin/create")
    public ResponseEntity<User> adminCreateUser(
            @RequestBody CreateUserRequest request,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(userService.createUser(request, actorId));
    }

    /**
     * US-BM3 — Branch Manager creates a Technician for their branch
     * POST /api/users/manager/create?actorId={managerId}
     * Role: BRANCH_MANAGER only
     * Body: { username, fullName, phone, role:"TECHNICIAN", branchId, password, skills:[] }
     */
    @PostMapping("/manager/create")
    public ResponseEntity<User> managerCreateTechnician(
            @RequestBody CreateUserRequest request,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(userService.createUser(request, actorId));
    }

    /**
     * US-A2 — Admin deactivates a user account
     * PATCH /api/users/{id}/deactivate?actorId={adminId}
     * Role: ADMIN only
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(
            @PathVariable Long id,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(userService.deactivateUser(id, actorId));
    }

    /**
     * US-A2 — Get all users (Admin view)
     * GET /api/users
     * Role: ADMIN only
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get a single user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Get all users in a branch
     * GET /api/users/branch/{branchId}
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<User>> getUsersByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(userService.getUsersByBranch(branchId));
    }

    /**
     * Get technicians only in a branch
     * GET /api/users/branch/{branchId}/technicians
     */
    @GetMapping("/branch/{branchId}/technicians")
    public ResponseEntity<List<User>> getTechniciansByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(userService.getTechniciansByBranch(branchId));
    }

    /**
     * Get skills of a technician
     * GET /api/users/{id}/skills
     */
    @GetMapping("/{id}/skills")
    public ResponseEntity<List<TechnicianSkill>> getTechnicianSkills(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getSkillsByTechnician(id));
    }
}
