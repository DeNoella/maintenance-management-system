package com.example.demo.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BranchRepository BranchRepository;
    private final TechnicianSkillRepository skillRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String fullName, String phone,
        Role role, Long branchId, String rawPassword, Long adminId) {

        if (userRepository.findByUsername(username).isPresent())
            throws new RuntimeException("username already exists")

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if(branchId != null) {
            Branch branch = branchRepository.findById(branchId)
               .orElseThrow(() -> new RuntimeException("Branch not found"));
            user.setBranch(branch);
        }

        User saved = userRepository.save(user);
        logActivity(adminId, "CREATE_USER", "USER", saved.getId(), "Created user:" + username);
        return saved;
}

    public User createTechnician(String username, String fullName, String phone,
        Long branchId, String rawPassword,
        List<String> skills, Long managerId) {
            User tech = createUser(username, fullName, phone, Role.TECHNICIAN, branchId, rawPassword, maanagerId);

            if(skills !=null) {
                skills.forEach(skillName -> {
                    TechnicianSkill skill = new TechnicianSkill();
                    skill.setTechnician(tech);
                    skill.setSkillName(skillName);
                    skillRepository.save(skill);
                });
            }
            return tech;
        }

    public User deactiveUser(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User bot found"));
        user.setIsActive(false);
        user.setUpdateAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        logActivity(adminId, "DEACTIVATE_USER", "User", userId, "Deactivated user: " + user.getUsername());
        return saved;
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public List<User> getUsersByBranch(Long branchId) {
        return userRepository.findByBranchId(branchId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
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