package com.example.demo.Repository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>{
    List<ActivityLog> findByUserId(Long userId);
    List<ActivityLog> findByUserIdAndPerformedAtBetween(Long userId, LocalDateTime form, LocalDateTime to);
    List<ActivityLog> findByActionType(String actionType);
    List<ActivityLog> findByEntityType(String entityType);
    List<ActivityLog> findByPerformedBetween(LocalDateTime from, LocalDateTime to);
}
