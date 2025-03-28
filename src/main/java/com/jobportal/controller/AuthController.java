package com.jobportal.controller;

import com.jobportal.dto.AuthRequest;
import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.UserDTO;
import com.jobportal.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register/jobseeker")
    @Operation(summary = "Register job seeker", description = "Register a new job seeker account")
    public ResponseEntity<UserDTO> registerJobSeeker(@Valid @RequestBody UserDTO request) {
        return ResponseEntity.ok(authService.registerJobSeeker(request));
    }

    @PostMapping("/register/employer")
    @Operation(summary = "Register employer", description = "Register a new employer account")
    public ResponseEntity<UserDTO> registerEmployer(@Valid @RequestBody UserDTO request) {
        return ResponseEntity.ok(authService.registerEmployer(request));
    }
}
