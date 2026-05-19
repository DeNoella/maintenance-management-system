package com.example.demo.Repository;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long>{
    Optional<AccessToken> findByTokenCode(String tokencode);
    List<AccessToken> findByTechnicianId(String technicianId);
    List<AccessToken> findByBranchId(Long branchId);
    List<AccessToken> findByStatusAndExpiresAtBefore(TokenStatus status, LocalDateTime now);
}
