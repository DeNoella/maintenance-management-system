package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStats {
    private long pendingRequests;
    private long activeTokens;
    private long totalUsers;
    private long totalBranches;
    private long totalCompanies;
    private long completedToday;
}
