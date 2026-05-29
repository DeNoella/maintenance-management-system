package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs",
         indexes = { @Index(columnList = "performed_at")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "action_type", length = 80)
    private String actionType;   // e.g create_user, verify_token

    @Column(name = "entity_type", length = 80)
    private String entityType;  // e.g. User, Branch

    @Column(name = "entity_id")
    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String details;      // JSON blob

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

}
