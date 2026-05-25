package com.example.mms.service;

import com.example.mms.dto.CreateUserRequest;
import com.example.mms.model.*;
import com.example.mms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final TechnicianSkillRepository skillRepository;
    private final ActivityLogService activityLogService;
    private final PasswordEncoder passwordEncoder;

    // US-A2: Admin creates Branch Manager or Technician
    public User createUser(CreateUserRequest req, Long actorId) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists: " + req.getUsername());
        }

        Role role = Role.valueOf(req.getRole());

        // Branch Manager cannot create Admin or another Branch Manager
        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found"));

        if (actor.getRole() == Role.BRANCH_MANAGER) {
            if (role != Role.TECHNICIAN) {
                throw new RuntimeException("Branch Managers can only create Technician accounts.");
            }
            // Branch Manager can only create technicians for their own branch
            if (!actor.getBranch().getId().equals(req.getBranchId())) {
                throw new RuntimeException("Branch Manager can only create technicians for their own branch.");
            }
        }

        Branch branch = null;
        if (req.getBranchId() != null) {
            branch = branchRepository.findById(req.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        User user = User.builder()
                .username(req.getUsername())
                .fullName(req.getFullName())
                .phone(req.getPhone())
                .role(role)
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .branch(branch)
                .isActive(true)
                .build();

        User saved = userRepository.save(user);

        // US-BM3: Save technician skills if provided
        if (role == Role.TECHNICIAN && req.getSkills() != null) {
            req.getSkills().forEach(skillName -> {
                TechnicianSkill skill = TechnicianSkill.builder()
                        .technician(saved)
                        .skillName(skillName)
                        .build();
                skillRepository.save(skill);
            });
        }

        activityLogService.log(actorId, "CREATE_USER", "User", saved.getId(),
                "Created user: " + saved.getUsername() + " role: " + role);

        return saved;
    }

    // US-A2: Deactivate user
    public User deactivateUser(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        User saved = userRepository.save(user);
        activityLogService.log(adminId, "DEACTIVATE_USER", "User", userId,
                "Deactivated: " + user.getUsername());
        return saved;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByBranch(Long branchId) {
        return userRepository.findByBranchId(branchId);
    }

    public List<User> getTechniciansByBranch(Long branchId) {
        return userRepository.findByBranchIdAndRole(branchId, Role.TECHNICIAN);
    }

    public List<TechnicianSkill> getSkillsByTechnician(Long technicianId) {
        return skillRepository.findByTechnicianId(technicianId);
    }
}
