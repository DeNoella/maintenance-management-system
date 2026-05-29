package com.example.demo.Controller;

import com.example.mms.dto.LoginRequest;
import com.example.mms.dto.LoginResponse;
import com.example.mms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(authService.login(request));
    }

}
