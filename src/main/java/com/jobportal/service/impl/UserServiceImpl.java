package com.jobportal.service.impl;

import com.jobportal.dto.UserDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.JobSeeker;
import com.jobportal.entity.User;
import com.jobportal.enums.UserRole;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobSeekerRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return convertToDto(user);
    }

    @Override
    public UserDTO getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return convertToDto(user);
    }

    @Override
    @Transactional
    public UserDTO updateUserProfile(UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        
        if (user.getRole() == UserRole.JOB_SEEKER) {
            JobSeeker jobSeeker = jobSeekerRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("JobSeeker", "id", user.getId()));
            
            jobSeeker.setSkills(userDTO.getSkills());
            jobSeeker.setExperience(userDTO.getExperience());
            jobSeeker.setEducation(userDTO.getEducation());
            jobSeeker.setResume(userDTO.getResume());
            
            jobSeekerRepository.save(jobSeeker);
            return convertToDto(jobSeeker);
        } else if (user.getRole() == UserRole.EMPLOYER) {
            Employer employer = employerRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employer", "id", user.getId()));
            
            employer.setCompanyName(userDTO.getCompanyName());
            employer.setIndustry(userDTO.getIndustry());
            employer.setCompanyDescription(userDTO.getCompanyDescription());
            employer.setWebsite(userDTO.getWebsite());
            
            employerRepository.save(employer);
            return convertToDto(employer);
        }
        
        userRepository.save(user);
        return convertToDto(user);
    }

    private UserDTO convertToDto(User user) {
        UserDTO.UserDTOBuilder builder = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone());
        
        if (user instanceof JobSeeker) {
            JobSeeker jobSeeker = (JobSeeker) user;
            builder.skills(jobSeeker.getSkills())
                   .experience(jobSeeker.getExperience())
                   .education(jobSeeker.getEducation())
                   .resume(jobSeeker.getResume());
        } else if (user instanceof Employer) {
            Employer employer = (Employer) user;
            builder.companyName(employer.getCompanyName())
                   .industry(employer.getIndustry())
                   .companyDescription(employer.getCompanyDescription())
                   .website(employer.getWebsite());
        }
        
        return builder.build();
    }
}
