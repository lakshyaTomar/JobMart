package com.jobportal.service;

import com.jobportal.dto.UserDTO;

public interface UserService {
    
    UserDTO findById(Long id);
    
    UserDTO getCurrentUserProfile();
    
    UserDTO updateUserProfile(UserDTO userDTO);
}
