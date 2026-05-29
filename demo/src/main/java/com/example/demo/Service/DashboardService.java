package com.example.demo.Service;

import com.example.demo.dto.DashboardStats;
import com.example.demo.Enum.*;
import com.example.demo.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MaintenanceRequestRepository requestRepository;
    private final AccessTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;

    // US-A1: Admin dashboard — system-wide summary
    public DashboardStats getAdminDashboard() {
        long pendingRequests = requestRepository.countByStatus(RequestStatus.PENDING);
        long activeTokens = tokenRepository.countByStatus(TokenStatus.ACTIVE);
        long totalUsers = userRepository.count();
        long totalBranches = branchRepository.count();
        long totalCompanies = companyRepository.count();
        long completedToday = requestRepository.findBySubmittedAtBetween(
                LocalDateTime.now().withHour(0).withMinute(0),
                LocalDateTime.now()
        ).stream().filter(r -> r.getStatus() == RequestStatus.COMPLETED).count();

        return new DashboardStats(
                pendingRequests,
                activeTokens,
                totalUsers,
                totalBranches,
                totalCompanies,
                completedToday
        );
    }

    // Branch Manager dashboard — scoped to their branch only (US-BM1)
    public DashboardStats getBranchManagerDashboard(Long branchId) {
        long pendingRequests = requestRepository.countByBranchIdAndStatus(branchId, RequestStatus.PENDING);
        long activeTokens = tokenRepository.countByBranchIdAndStatus(branchId, TokenStatus.ACTIVE);
        long totalUsers = userRepository.findByBranchId(branchId).size();

        return new DashboardStats(
                pendingRequests,
                activeTokens,
                totalUsers,
                1L,   // one branch
                0L,
                0L
        );
    }
}
