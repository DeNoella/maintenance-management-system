package com.example.mms.service;

import com.example.mms.dto.CreateCompanyRequest;
import com.example.mms.model.*;
import com.example.mms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final CompanyServiceRepository companyServiceRepository;
    private final ActivityLogService activityLogService;

    // US-A3: Create company with services
    public Company createCompany(CreateCompanyRequest req, Long adminId) {
        if (companyRepository.existsByName(req.getName())) {
            throw new RuntimeException("Company name already exists.");
        }

        Company company = Company.builder()
                .name(req.getName())
                .robCertificate(req.getRobCertificate())
                .phone(req.getPhone())
                .address(req.getAddress())
                .isActive(true)
                .build();

        Company saved = companyRepository.save(company);

        // Attach services
        if (req.getServiceIds() != null) {
            req.getServiceIds().forEach(serviceId -> {
                ServiceType serviceType = serviceTypeRepository.findById(serviceId)
                        .orElseThrow(() -> new RuntimeException("Service not found: " + serviceId));
                CompanyService cs = CompanyService.builder()
                        .company(saved)
                        .service(serviceType)
                        .build();
                companyServiceRepository.save(cs);
            });
        }

        activityLogService.log(adminId, "CREATE_COMPANY", "Company", saved.getId(),
                "Created company: " + saved.getName());
        return saved;
    }

    // US-A3: Edit company
    public Company updateCompany(Long companyId, CreateCompanyRequest req, Long adminId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(req.getName());
        company.setPhone(req.getPhone());
        company.setAddress(req.getAddress());
        company.setRobCertificate(req.getRobCertificate());
        Company saved = companyRepository.save(company);
        activityLogService.log(adminId, "UPDATE_COMPANY", "Company", companyId,
                "Updated company: " + saved.getName());
        return saved;
    }

    // US-A3: Deactivate company
    public Company deactivateCompany(Long companyId, Long adminId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setIsActive(false);
        Company saved = companyRepository.save(company);
        activityLogService.log(adminId, "DEACTIVATE_COMPANY", "Company", companyId, "Deactivated company");
        return saved;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public List<ServiceType> getAllServices() {
        return serviceTypeRepository.findAll();
    }

    public List<CompanyService> getServicesByCompany(Long companyId) {
        return companyServiceRepository.findByCompanyId(companyId);
    }
}
