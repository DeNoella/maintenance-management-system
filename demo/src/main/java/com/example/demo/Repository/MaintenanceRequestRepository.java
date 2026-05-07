package com.example.demo.Repository;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long>{
    
    List<MaintenanceRequest> findByTechnicianId(Long technicianId);
    List<MaintenanceRequest> findByBranchId(Long branchId);
    List<MaintenanceRequest> findByStatus(RequestStatus status);
    List<MaintenanceRequest> findByBranchIdAndStatus(Long branchId, RequestStatus status);
    List<MaintenanceRequest> findBySubmittedAtBetween(LocalDateTime from, LocalDateTime to);
}
