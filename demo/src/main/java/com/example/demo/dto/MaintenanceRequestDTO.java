package com.example.demo.dto;

import lombok.Data;

@Data
public class MaintenanceRequestDTO {
    private Long technicianId;
    private Long branchId;
    private String issueDescription;
    private String priority;            // LOW, MEDIUM, HIGH, URGENT
    private String preferredVisitDate;  // FORMAT: YYYY-MM-DD
}
