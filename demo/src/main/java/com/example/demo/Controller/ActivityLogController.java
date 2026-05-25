package com.example.demo.Controller;

import com.example.mms.model.ActivityLog;
import com.example.mms.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    /**
     * US-A5 / US-BM4 / US-T4 — Any user views their own activity log
     * GET /api/activity-logs/user/{userId}
     * Role: authenticated (each user sees only their own)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityLog>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(activityLogService.getByUser(userId));
    }

    public ResponseEntity<List<ActivityLog>> getByUserAndRange(
             @PathVariable Long userId,
             @RequestParam String from,
             @RequestParam String to) {
        return ResponseEntity.ok(activityLogService.getByUserAndDateRange(
            userId,
            LocalDateTime.parse(from),
            LocalDateTime.parse(to)
        ));
    }

    /**
     * US-A6 — Admin views all system-wide activity logs
     * GET /api/activity-logs
     * Role: ADMIN only
     */
    public ResponseEntity<List<ActivityLog>> getAll() {
        return ResponseEntity.ok(activityLogService.getAll());
    }

    /**
     * US-A6 — Admin filters system-wide logs by date range
     * GET /api/activity-logs/range?from=...&to=...
     * Role: ADMIN only
     */
    public ResponseEntity<List<ActivityLog>> getAllByRange(
          @RequestParam String from,
          @RequestParam String to) {
        return ResponseEntity.ok(activityLogService.getAllByDateRange(
                LocalDateTime.parse(from),
                LocalDateTime.parse(to)
        ));
        }
    
    /**
     * Filter logs by action type
     * GET /api/activity-logs/action/{actionType}
     * e.g. actionType = CREATE_USER, VERIFY_TOKEN, SUBMIT_REQUEST
     */
    public ResponseEntity<List<ActivityLog>> getByActionType(@PathVariable String actionType) {
        return ResponseEntity.ok(activityLogService.getByActionType(actionType));
    }

}
