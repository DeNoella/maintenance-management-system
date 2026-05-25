package com.example.demo.Repository;

import com.example.mms.model.TokenVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenVerificationRepository extends JpaRepository<TokenVerification, Long>{
    List<TokenVerification> findByTokenId(Long tokenId);
    List<TokenVerification> findByVerifiedById(Long userId);
    List<TokenVerification> findByToken_BranchId(Long branchId);
}
