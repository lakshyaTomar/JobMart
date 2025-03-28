package com.jobportal.service;

import com.jobportal.dto.AuthRequest;
import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.UserDTO;

public interface AuthService {
    
    AuthResponse authenticate(AuthRequest request);
    
    UserDTO registerJobSeeker(UserDTO request);
    
    UserDTO registerEmployer(UserDTO request);
}
