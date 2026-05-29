package com.example.mms.controller;

import com.example.mms.dto.CreateCompanyRequest;
import com.example.mms.model.Company;
import com.example.mms.model.CompanyService;
import com.example.mms.model.ServiceType;
import com.example.mms.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * US-A3 — Admin creates a company with services
     * POST /api/companies?actorId={adminId}
     * Role: ADMIN only
     * Body: { name, robCertificate, phone, address, serviceIds:[] }
     */
    @PostMapping
    public ResponseEntity<Company> createCompany(
            @RequestBody CreateCompanyRequest request,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(companyService.createCompany(request, actorId));
    }

    /**
     * US-A3 — Admin updates a company
     * PUT /api/companies/{id}?actorId={adminId}
     * Role: ADMIN only
     */
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long id,
            @RequestBody CreateCompanyRequest request,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(companyService.updateCompany(id, request, actorId));
    }

    /**
     * US-A3 — Admin deactivates a company
     * PATCH /api/companies/{id}/deactivate?actorId={adminId}
     * Role: ADMIN only
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Company> deactivateCompany(
            @PathVariable Long id,
            @RequestParam Long actorId) {
        return ResponseEntity.ok(companyService.deactivateCompany(id, actorId));
    }

    /**
     * US-A3 — Get all companies
     * GET /api/companies
     * Role: ADMIN only
     */
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    /**
     * Get a single company by ID
     * GET /api/companies/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getById(id));
    }

    /**
     * Get all available service types
     * GET /api/companies/services
     */
    @GetMapping("/services")
    public ResponseEntity<List<ServiceType>> getAllServices() {
        return ResponseEntity.ok(companyService.getAllServices());
    }

    /**
     * Get services assigned to a specific company
     * GET /api/companies/{id}/services
     */
    @GetMapping("/{id}/services")
    public ResponseEntity<List<CompanyService>> getCompanyServices(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getServicesByCompany(id));
    }
}
