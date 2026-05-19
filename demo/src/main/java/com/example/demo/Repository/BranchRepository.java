package com.example.demo.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long>{
    List<Branch> findByCompanyId(Long companyId);
    List<Branch> findByIsActiveTrue();
}
