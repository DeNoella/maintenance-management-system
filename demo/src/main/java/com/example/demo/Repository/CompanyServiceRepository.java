package com.example.demo.Repository;

import com.example.demo.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyServiceRepository extends JpaRepository<CompanyService, Long>{
    List<CompanyService> findByCompanyId(Long companyId);
    void deleteByCompanyIdAndServiceId(Long companyId, Long serviceId);
}
