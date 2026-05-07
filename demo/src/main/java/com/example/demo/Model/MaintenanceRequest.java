package com.example.demo.Model;

@Entity
@Table(name = "maintenance_requests", 
    indexes = {
        @Index(columnList = "status"),
        @Index(columnList = "submitted_at")
    })
public class MaintenanceRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private User technician;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "issue_description", columnDefinition = "TEXT")
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "preferred_visit_date")
    private LocalDate preferredVisitDate;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
