package com.example.demo.dto;

import lombok.Data;

@Data
public class TokenVerifyRequest {
    private String tokenCode;
    private Long managerId;
    private String notes;
}
