package com.example.mms.service;

import com.example.mms.dto.TokenVerifyRequest;
import com.example.mms.model.*;
import com.example.mms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final AccessTokenRepository tokenRepository;
    private final TokenVerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    // US-T3: Technician views their own tokens
    public List<AccessToken> getTokensByTechnician(Long technicianId) {
        return tokenRepository.findByTechnicianId(technicianId);
    }

    // US-BM2: Branch Manager scans/verifies token
    public TokenVerification verifyToken(TokenVerifyRequest req) {
        AccessToken token = tokenRepository.findByTokenCode(req.getTokenCode())
                .orElseThrow(() -> new RuntimeException("Token not found. Not Authorized."));

        User manager = userRepository.findById(req.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        // Decide GRANTED or DENIED based on token state
        Decision decision;
        if (token.getStatus() == TokenStatus.ACTIVE
                && token.getExpiresAt().isAfter(LocalDateTime.now())) {
            decision = Decision.GRANTED;
        } else if (token.getStatus() == TokenStatus.EXPIRED
                || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            decision = Decision.DENIED; // "Expired"
        } else {
            decision = Decision.DENIED; // "Not Authorized" (REVOKED or invalid)
        }

        TokenVerification verification = TokenVerification.builder()
                .token(token)
                .verifiedBy(manager)
                .decision(decision)
                .notes(req.getNotes())
                .build();

        TokenVerification saved = verificationRepository.save(verification);

        activityLogService.log(req.getManagerId(), "VERIFY_TOKEN",
                "AccessToken", token.getId(),
                "Decision: " + decision + " for token: " + req.getTokenCode());

        return saved;
    }

    // Admin can revoke a token
    public AccessToken revokeToken(Long tokenId, Long adminId) {
        AccessToken token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        token.setStatus(TokenStatus.REVOKED);
        AccessToken saved = tokenRepository.save(token);
        activityLogService.log(adminId, "REVOKE_TOKEN", "AccessToken", tokenId, "Token revoked");
        return saved;
    }

    // Called by scheduler — flips ACTIVE → EXPIRED when expiresAt passes
    public void expireOverdueTokens() {
        List<AccessToken> overdueTokens = tokenRepository
                .findByStatusAndExpiresAtBefore(TokenStatus.ACTIVE, LocalDateTime.now());
        overdueTokens.forEach(t -> t.setStatus(TokenStatus.EXPIRED));
        tokenRepository.saveAll(overdueTokens);
    }

    public List<AccessToken> getByBranch(Long branchId) {
        return tokenRepository.findByBranchId(branchId);
    }

    public List<AccessToken> getAll() {
        return tokenRepository.findAll();
    }
}
