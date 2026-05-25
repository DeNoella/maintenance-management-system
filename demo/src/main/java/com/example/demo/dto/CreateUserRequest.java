package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateUserRequest {
    private String username;
    private String fullname;
    private String phone;
    private String role;      // BRANCH_MANAGER or TECHNICIAN
    private Long branchId;    // null for Admin
    private String password;
    private List<String> skills;        // only used when creating TECHNICIAN (US-BM3)
}
