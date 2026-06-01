package com.example.demo.Controller;

import com.example.demo.dto.TokenVerifyRequest;
import com.example.demo.Model.AccessToken;
import com.example.demo.Model.TokenVerification;
import com.example.demo.Service.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class AccessTokenController {

    private final AccessTokenService tokenService;

    /**
     * US-T3 — Technician views their own issued tokens
     * GET /api/tokens/technician/{techId}
     * Role: TECHNICIAN
     * Returns: list of tokens with branch name, visit date, status (active/expired)
     */
    @GetMapping("/technician/{techId}")
public ResponseEntity<List<AccessToken>> getByTechnician(
        @PathVariable Long techId) {

    return ResponseEntity.ok(
            tokenService.getTokensByTechnician(techId)
    );
}

    /**
     * US-BM2 — Branch Manager scans/verifies a technician token
     * POST /api/tokens/verify
     * Role: BRANCH_MANAGER
     * Body: { tokenCode, managerId, notes }
     * Returns: decision (GRANTED or DENIED) + technician info + validity
     */
    @PostMapping("/verify")
    public ResponseEntity<TokenVerification> verifyToken(
        @RequestBody TokenVerifyRequest request) {
      return ResponseEntity.ok(tokenService.verifyToken(request));
        }

    /**
     * Admin revokes a token
     * PATCH /api/tokens/{id}/revoke?actorId={adminId}
     * Role: ADMIN
     */
    
    @PatchMapping("{id}/revoke")
    public ResponseEntity<AccessToken> revoketoken(
             @PathVariable Long id,
             @RequestParam Long actorId) {
        return ResponseEntity.ok(tokenService.revokeToken(id, actorId));
        }

     /**
     * US-BM1 — Branch Manager views all tokens for their branch
     * GET /api/tokens/branch/{branchId}
     * Role: BRANCH_MANAGER
     */
    @GetMapping("/branch/{branchId}")    
    public ResponseEntity<List<AccessToken>> getByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(tokenService.getByBranch(branchId));
    }

    /**
     * Admin views all tokens system-wide
     * GET /api/tokens
     * Role: ADMIN
     */
    @GetMapping
    public ResponseEntity<List<AccessToken>> getAll() {
        return ResponseEntity.ok(tokenService.getAll());
    }

}
