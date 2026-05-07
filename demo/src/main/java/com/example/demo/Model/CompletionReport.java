package com.example.demo.Model;

@Entity
@Table(name="Completion_reports")
public class CompletionReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id",nullable = false)
    private MaintenanceRequest request;

    @ManyToOne
    @JoinColumn(name= "technician_id", nullable-false)
    private User technician;

    private String workSummary;

    @Enumerated(EnumType.STRING)
    @Column(name= "approval_status")
    private AppprovalStatus approvalStatus = AppprovalStatus.PENDING;

    @ManyTonOne
    @JoinColumn(name = "approved_by") //This will be null until approved 
    private User approvedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

}
