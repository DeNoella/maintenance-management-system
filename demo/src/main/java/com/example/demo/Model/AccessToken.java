package com.example.demo.Model;

@Entity
@Table(name = "access_tokens", 
    indexes = {
        @Index(columnList = "token_code"),
        @Index(columnList = "expires_at")
    })
public class AccessToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private MaintenanceRequest request;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private User technician;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "token_code", unique = true, length = 100)
    private String tokenCode; 

    @Column(name = "qr_data", columnDefinition = "TEXT")
    private String qrData;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "expired_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
