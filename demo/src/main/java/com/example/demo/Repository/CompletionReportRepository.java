package com.example.demo.Repository;

@Repository
public interface CompletionReportRepository extends JpaRepository<CompletionReport, Long> {
    Optional<CompletionReport> findByRequestId(Long requestId);
    List<CompletionReport> findByTechnicianId(Long technicianId);
    List<CompletionReport> findByApprovalStatus(ApprovalStatus status);
}
