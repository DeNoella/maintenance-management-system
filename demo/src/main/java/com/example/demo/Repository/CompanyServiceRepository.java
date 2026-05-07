package com.example.demo.Repository;

@Repository
public interface CompanyServiceRepository extends JpaRepository<CompanyService, Long>{
    List<CompanyService> findByCompanyId(Long companyId);
    void deleteByCompanyIdAndServiceId(Long companyId, Long serviceId);
}
