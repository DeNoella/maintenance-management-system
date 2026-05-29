package com.example.demo.dto;

import lombok.Data;

@Data
public class CompletionReportRequest {
    private Long requestId;
    private Long technicianId;
    private String workSummary;
}
