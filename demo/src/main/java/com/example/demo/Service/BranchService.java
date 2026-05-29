package com.example.mms.service;

import com.example.mms.dto.CreateBranchRequest;
import com.example.mms.model.Branch;
import com.example.mms.model.Company;
import com.example.mms.repository.BranchRepository;
import com.example.mms.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;
    private final ActivityLogService activityLogService;

    // US-A4: Create branch
    public Branch createBranch(CreateBranchRequest req, Long adminId) {
        Company company = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Branch branch = Branch.builder()
                .name(req.getName())
                .address(req.getAddress())
                .company(company)
                .isActive(true)
                .build();

        Branch saved = branchRepository.save(branch);
        activityLogService.log(adminId, "CREATE_BRANCH", "Branch", saved.getId(),
                "Created branch: " + saved.getName());
        return saved;
    }

    // US-A4: Update branch
    public Branch updateBranch(Long branchId, CreateBranchRequest req, Long adminId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setName(req.getName());
        branch.setAddress(req.getAddress());
        Branch saved = branchRepository.save(branch);
        activityLogService.log(adminId, "UPDATE_BRANCH", "Branch", branchId,
                "Updated branch: " + saved.getName());
        return saved;
    }

    // US-A4: Deactivate branch
    public Branch deactivateBranch(Long branchId, Long adminId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setIsActive(false);
        Branch saved = branchRepository.save(branch);
        activityLogService.log(adminId, "DEACTIVATE_BRANCH", "Branch", branchId, "Deactivated branch");
        return saved;
    }

    public List<Branch> getAll() {
        return branchRepository.findAll();
    }

    public List<Branch> getByCompany(Long companyId) {
        return branchRepository.findByCompanyId(companyId);
    }

    public Branch getById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }
}
