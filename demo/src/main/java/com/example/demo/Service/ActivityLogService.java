package com.example.demo.Service;

import com.example.demo.Model.ActivityLog;
import com.example.demo.Model.User;
import com.example.demo.Repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository ActivityLogRepository;
    private final UserRepository userRepository;

    public void log(Long userId, String actionType, Long entityId, String details, String entityType) {
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
        return ActivityLogRepository.findByUserId(userId);
    }

    public List<ActivityLog> getByUserAndDateRange(Long userId, LocalDateTime from, LocalDateTime to) {
        return ActivityLogRepository.findByUserIdAndPerformedAtBetween(userId, from, to);
    }

    public List<ActivityLog> getAll() {
        return ActivityLogRepository.findAll();
    }

    public List<ActivityLog> getAllByDateRange(LocalDateTime from, LocalDateTime to) {
        return ActivityLogRepository.findByPerformedAtBetween(from, to);
    }

    public List<ActivityLog> getByActionType(String actionType) {
        return ActivityLogRepository.findByActionType(actionType);
    }

}
