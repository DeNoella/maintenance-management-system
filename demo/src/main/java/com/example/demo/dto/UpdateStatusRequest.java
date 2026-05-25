package com.example.demo.dto;

import lombok.Data;

@Data
public class UpdateStatusRequest {
    private String status;          // APPROVED, REJECTED, IN_PROGRESS, COMPLETED
    private Long managerId;
}
