package com.example.demo.Repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Enum.ApprovalStatus;
import com.example.demo.Model.CompletionReport;

@Repository
public interface CompletionReportRepository extends JpaRepository<CompletionReport, Long> {
    Optional<CompletionReport> findByRequestId(Long requestId);
    List<CompletionReport> findByTechnicianId(Long technicianId);
    List<CompletionReport> findByApprovalStatus(ApprovalStatus status);
    List<CompletionReport> findByRequest_BranchId(Long branchId);
}
