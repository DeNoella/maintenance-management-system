package com.example.demo.Service;

import com.example.mms.model.ActivityLog;
import com.example.mms.model.User;
import com.example.mms.repository.ActivityLogRepository;
import com.example.mms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository ActivityLogRepository;
    private final UserRepository userRepository;

    public void log(Long userId, String actionType, Long entityId, String details) {
        User user = userRepository.findById(userId).orElse(null);
        ActivityLog log = ActivityLog.builder()
                .user(user)
                .actionType(actionType)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .performedAt(LocalDateTime.now())
                .build();
        ActivityLogRepository.save(log);
    }

    public List<ActivityLog> getByUser(Long userId) {
        return activityLogRepository.findByUserId(userId);
    }

    public List<ActivityLog> getByUserAndDateRange(Long userId, LocalDaetTime from, LocalDateTime to) {
        return activityLogRepository.findByUserIdAndPerformedAtBetween(userId, from, to);
    }

    public List<ActivityLog> getAll() {
        return activityLogRepository.findAll();
    }

    public List<ActivityLog> getAllByDateRange(LocalDateTime from, LocalDateTime to) {
        return activityLogRepository.findByPerformedAtBetween(from, to);
    }

    public List<ActivityLog> getByActionType(String actionType) {
        return activityLogRepository.findByActionType(actionType);
    }

}
