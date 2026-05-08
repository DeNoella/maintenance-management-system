package com.example.demo.Service;

@Service
@RequiredArgsConstructor
public class MaintenanceRequestService {

    private final MaintenanceRequest requestRepository;
    private final AccessTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ActivityLogRepository activityLogRepository;

    public MaintenanceRequest submitRequest(Long technicianId, Long branchId,
                              String issueDescription, Priority priority,
                              LocalDate preferredVisitDate) {
        User tech = userRepository.findById(technicianId)
            .orElseThrow(() -> new RuntimeException("Technician not found"));
        Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new RuntimeException("Branch not found"));
        
        MaintenanceRequest req = new MaintenanceRequest();
        req.setTechnician(tech);
        req.setBranch(branch);
        req.setIssueDescription(issueDescription);
        req.setPriority(priority);
        req.setPreferresVisitDate(preferredVisitDate);
        req.setSubmittedAt(LocalDateTime.now());

        MaintenanceRequest saved = requestRepository.save(req);
        logActivity(technicianId, "SUBMIT_REQUEST","MaintenanceRequest", saved.getId(), "Submitted maintenance request");
        return saved;
    }

    public MaintenanceRequest updateStatus(Log requestId, RequestStatus newStatus, Long managerId) {
        MaintenanceRequest req = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        req.setStatus(newStatus);
        req.setUpdatedAt(LocalDateTime.now());
        MaintenanceRequest saved = requestRepository.save(req);

        if (newStatus == RequestStatus.APPROVED) {
            generateToken(saved);
        }
        logActivity(managerId, "UPDATE_REQUEST_STATUS", "MaintenanceRequest", requestId,
            "Status changed to: " + newStatus);
            return saved;
    }

    private void generateToken(MaintenanceRequest request) {
        AccessToken token = new AccessToken();
        token.setRequest(request);
        token.setTechnician(request.getTechnician());
        token.setBranch(request.getBranch());
        token.setTokenCode(UUID.randomUUID().toString());
        token.setQrData("MMMS-TOKEN" + token.getTokenCode());
        token.setStatus(TokenStatus.ACTIVE);
        token.setValideFrom(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(7));
        token.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
    
    public List<MaintenanceRequest> getByTechnician(Long techId) {
        return requestRepository.findByTechnicianId(techId);
    }

    public List<MaintenanceRequest> getByBranch(Long branchId) {
        return requestRepository.findByBranchId(branchId);
    }

    private List<MaintenanceRequest> getAll() { return requestRepository.findAll(); }

    private void logActivity(Long userId, String action, String entityType, Long entityId, String details) {
        Activity log = new ActivityLog();
        log.setActionType(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setPerformedAt(LocalDateTime.now());
        activityLogRepository.save(log);
    
    }
}
