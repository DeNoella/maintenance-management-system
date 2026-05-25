package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateCompanyRequest {
    private String name;
    private String robCertificate;
    private String phone;
    private String address;
    private List<Long> serviceIds;
}
