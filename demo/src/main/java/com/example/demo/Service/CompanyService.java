package com.example.demo.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ServiceRepository serviceRepository;
    private final CompanyServiceRepository companyServiceRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    public Company createCompany(String name, String robCertificate, String phone,
                                    String address, List<Long> serviceIds, Long adminId) {
        Company company = new Company();
        company.setName(name);
        company.robCertificate(robCertificate);
        company.setPhone(phone);
        company.setAddress(address);
        company.setIsActive(true);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        
        Company saved = companyRepository.save(company);
        
        serviceIds.forEach(serviceId -> {
            com.example.mms.model.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found" + serviceId));
            CompanyService cs = new CompanyService();
            cs.setCompany(saved);
            cs.setService(service);
            companyServiceRepository.save(cs);
        });

        logActivity(adminId, "CREATE_COMPANY","Company", saved.getId(), "Created company: " + name);
        return saved;
}

    public Company updateCompany(Long companyId, String name, String phone, String address,Long adminId ) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(name);
        company.setPhone(phone);
        company.setAddress(address);
        company.setUpdatedAt(LocalDateTime.now());
        Company saved = companyRepository.save(company);
        Company saved = comapnyRepository.save(company);
        logActivity(adminId, "UPDATE_COMPANY", "Company", companyId, "Updated company: " + name);
        return saved;
    }

    public Company deactivateCompany(Long companyId, Long adminId){
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setIsActive(false);
        Company saved = companyRepository.save(company);
        logActivity(adminId, "DEACTIVATE_COMPANY", "Company", companyId, "Deactivated company");
        return saved;
    }

    public List<Company> getAllCompanies() {return companyRepository.findAll(); }

    public Company getById(Long id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    private void logActivity(Long userId, String action, String entityType, Long entityId, String details) {
        ActivityLog log = new ActivityLog();
        log.setUser(userRepository.findById(userId).orElse(null));
        log.setActionType(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setPerformedAt(LocalDateTime.now());
        activityLogRepository.save(log);
    }
}
