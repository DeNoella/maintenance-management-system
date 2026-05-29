package com.example.demo.Service;

import com.example.mms.dto.LoginRequest;
import com.example.mms.dto.LoginResponse;
import com.example.mms.model.User;
import com.example.mms.repository.UserRepository;
import com.example.mms.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ActivityLogService activityLogService;

    // US-A1 / US-T1 — Login for all roles
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated. Contact your Admin.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

    // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getId());

        activityLogService.log(user.getId(), "LOGIN", "User", user.getId(),
                "User logged in: " + user.getUsername());

        return new LoginResponse(token, user.getRole().name(), user.getId(), user.getFullName());
    }

}
