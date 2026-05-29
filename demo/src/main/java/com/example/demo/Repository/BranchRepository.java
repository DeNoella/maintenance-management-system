package com.example.demo.Repository;

import com.example.mms.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long>{
    List<Branch> findByCompanyId(Long companyId);
    List<Branch> findByIsActiveTrue();
}
