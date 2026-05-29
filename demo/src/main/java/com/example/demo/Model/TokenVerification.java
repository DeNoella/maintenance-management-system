package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.example.demo.Enum.Decision;

@Entity
@Table(name = "token_verifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = false)
    private AccessToken token;

    @ManyToOne
    @JoinColumn(name = "verified_by", nullable = false)
    private User verifiedBy;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @PrePersist
    protected void onCreate() {
        verifiedAt = LocalDateTime.now();
    }
}
