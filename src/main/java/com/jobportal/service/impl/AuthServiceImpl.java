package com.jobportal.service.impl;

import com.jobportal.dto.AuthRequest;
import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.UserDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.JobSeeker;
import com.jobportal.entity.User;
import com.jobportal.enums.UserRole;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobSeekerRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.security.JwtTokenUtil;
import com.jobportal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenUtil.generateToken(userDetails, user);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public UserDTO registerJobSeeker(UserDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        JobSeeker jobSeeker = JobSeeker.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.JOB_SEEKER)
                .phone(request.getPhone())
                .skills(request.getSkills())
                .experience(request.getExperience())
                .education(request.getEducation())
                .resume(request.getResume())
                .build();

        JobSeeker savedJobSeeker = jobSeekerRepository.save(jobSeeker);

        return UserDTO.builder()
                .id(savedJobSeeker.getId())
                .name(savedJobSeeker.getName())
                .email(savedJobSeeker.getEmail())
                .role(savedJobSeeker.getRole())
                .phone(savedJobSeeker.getPhone())
                .skills(savedJobSeeker.getSkills())
                .experience(savedJobSeeker.getExperience())
                .education(savedJobSeeker.getEducation())
                .resume(savedJobSeeker.getResume())
                .build();
    }

    @Override
    @Transactional
    public UserDTO registerEmployer(UserDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        Employer employer = Employer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.EMPLOYER)
                .phone(request.getPhone())
                .companyName(request.getCompanyName())
                .industry(request.getIndustry())
                .companyDescription(request.getCompanyDescription())
                .website(request.getWebsite())
                .build();

        Employer savedEmployer = employerRepository.save(employer);

        return UserDTO.builder()
                .id(savedEmployer.getId())
                .name(savedEmployer.getName())
                .email(savedEmployer.getEmail())
                .role(savedEmployer.getRole())
                .phone(savedEmployer.getPhone())
                .companyName(savedEmployer.getCompanyName())
                .industry(savedEmployer.getIndustry())
                .companyDescription(savedEmployer.getCompanyDescription())
                .website(savedEmployer.getWebsite())
                .build();
    }
}
