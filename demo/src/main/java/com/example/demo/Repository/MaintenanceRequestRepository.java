package com.example.demo.Repository;

import com.example.mms.model.MaintenanceRequest;
import com.example.mms.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long>{
    
    List<MaintenanceRequest> findByTechnicianId(Long technicianId);
    List<MaintenanceRequest> findByBranchId(Long branchId);
    List<MaintenanceRequest> findByStatus(RequestStatus status);
    List<MaintenanceRequest> findByBranchIdAndStatus(Long branchId, RequestStatus status);
    List<MaintenanceRequest> findBySubmittedAtBetween(LocalDateTime from, LocalDateTime to);
    List<MaintenanceRequest> findByBranchIdAndSubmittedAtBetween(Long branchId, LocalDateTime from, LocalDateTime to);
    long countByStatus(RequestStatus status);
    long countByBranchIdAndStatus(Long branchId, RequestStatus status);
}
