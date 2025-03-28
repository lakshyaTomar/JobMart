package com.jobportal.controller;

import com.jobportal.dto.AuthRequest;
import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.UserDTO;
import com.jobportal.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register/jobseeker")
    public ResponseEntity<UserDTO> registerJobSeeker(@Valid @RequestBody UserDTO request) {
        return ResponseEntity.ok(authService.registerJobSeeker(request));
    }

    @PostMapping("/register/employer")
    public ResponseEntity<UserDTO> registerEmployer(@Valid @RequestBody UserDTO request) {
        return ResponseEntity.ok(authService.registerEmployer(request));
    }
}
