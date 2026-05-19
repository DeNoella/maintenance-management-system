package com.example.demo.Repository;

@Repository
public interface TokenVerificationRepository extends JpaRepository<TokenVerification, Long>{
    List<TokenVerification> findByTokenId(Long tokenId);
    List<TokenVerification> findByVerifiedBy_Id(Long userId);
}
