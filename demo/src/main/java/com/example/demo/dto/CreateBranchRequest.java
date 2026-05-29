package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateBranchRequest {
    private String name;
    private String address;
    private Long companyId;
}
