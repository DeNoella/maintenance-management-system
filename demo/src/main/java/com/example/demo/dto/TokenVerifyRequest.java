package com.example.demo.dto;

import lombok.Data;

@Data
public class TokenVerifyRequest {
    private String tokrnCode;
    private Long managerId;
    private String notes;
}
