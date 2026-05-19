package com.example.demo.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByUsername(String username);
    List<User> findByBranchId(Long branchId);
    List<User> findByRole(Role role);
    List<User> findByBranchIdAndRole(Long branchId, Role role);
}
