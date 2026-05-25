package com.example.demo.dto;

import lombok.Data;

@Data
public class ReviewReportRequest {
    private Long managerId;
    private String decision;    //APPROVED or REJECTED
}

