package com.example.demo.Repository;

import com.example.mms.model.AccessToken;
import com.example.mms.model.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long>{
    Optional<AccessToken> findByTokenCode(String tokencode);
    List<AccessToken> findByTechnicianId(String technicianId);
    List<AccessToken> findByBranchId(Long branchId);
    List<AccessToken> findByStatusAndExpiresAtBefore(TokenStatus status, LocalDateTime now);
    long countByStatus(TokenStatus status);
    long countByBranchIdAndStatus(Long branchId, TokenStatus status);
}
