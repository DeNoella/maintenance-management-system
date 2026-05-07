package com.example.demo.Service;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;


    public Branch createBranch(String name, String address, Long companyId, Long adminId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        Branch branch = new Branch();
        branch.setName(name);
        branch.setAddress(address);
        branch.setCompany(company);
        branch.setIsActive(true);
        branch.setCreatedAt(LocalDateTime.now());
        branch.setUpdatedAt(LocalDateTime.now());

        Branch saved = branchRepository.save(branch);
        logActivity(adminId, "CREATE_BRANCH", "Branch", saved.getId(), "Created branch: " + name);
        return saved;
    }

    public Branch updateBranch(Long branchId, String name, String address, Long adminId){
        Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setName(name);
        branch.setAddress(address);
        branch.setUpdatedAt(LocalDateTime.now());
        Branch saved = branchRepository.save(branch);
        logActivity(adminId, "UPDATE_BRANCH", "Branch", branchId, "Updated branch: " + name);
        return saved;
    }

    public Branch deactivateBranch(Long branchId, Long adminId) {
        Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setIsActive(false);
        Branch saved = branchRepository.save(branch);
        logActivity(adminId, "DEACTIVATE_BRANCH", "Branch", branchId, "Deactivated branch");
        return saved;
    }

    public List<Branch> getAll() { return branchRepository.findAll(); }
    public List<Branch> getCompany(Long companyId) { return branchRepository.findByCompanyId(companyId); }

    private void logActivity(Long userId, String action, String entityType, Long entityId, String details) {
        ActivityLog log = new ActivityLog();
        log.setUser(UserRepository.findById(userId).orElse(null));
        log.setActionType(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setPerformedAt(LocalDateTime.now());
        activityLogRepository.save(log);
    }
}
