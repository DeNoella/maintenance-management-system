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
        request 

                              }
    




}
