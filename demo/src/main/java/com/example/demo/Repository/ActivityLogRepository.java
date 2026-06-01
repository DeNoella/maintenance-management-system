package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>{
    List<ActivityLog> findByUserId(Long userId);
    List<ActivityLog> findByUserIdAndPerformedAtBetween(Long userId, LocalDateTime form, LocalDateTime to);
    List<ActivityLog> findByActionType(String actionType);
    List<ActivityLog> findByEntityType(String entityType);
    List<ActivityLog> findByPerformedAtBetween(LocalDateTime from, LocalDateTime to);
}
