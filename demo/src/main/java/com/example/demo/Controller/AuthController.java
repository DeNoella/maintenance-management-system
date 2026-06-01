package com.example.demo.Controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService AuthService;

    /**
     * US-A1 / US-T1 — Login for ALL roles (Admin, Branch Manager, Technician)
     * POST /api/auth/login
     * Body: { "username": "...", "password": "..." }
     * Returns: JWT token + role + userId + fullName
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(AuthService.login(request));
    }


}
