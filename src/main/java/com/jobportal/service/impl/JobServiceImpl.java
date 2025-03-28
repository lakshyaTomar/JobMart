package com.jobportal.service.impl;

import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobSearchCriteria;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.enums.JobType;
import com.jobportal.enums.UserRole;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;

    @Override
    public Page<JobDTO> findJobs(JobSearchCriteria searchCriteria, Pageable pageable) {
        String jobTypeStr = searchCriteria.getJobType() != null ? searchCriteria.getJobType().name() : null;
        
        // Debug: List all jobs before filtering
        System.out.println("All jobs in database:");
        jobRepository.findAll().forEach(job -> 
            System.out.println("Job ID: " + job.getId() + 
                              ", Title: " + job.getTitle() + 
                              ", Active: " + job.isActive() + 
                              ", Created: " + job.getCreatedAt()));
        
        Page<Job> jobs = jobRepository.findBySearchCriteria(
                searchCriteria.getKeyword(),
                searchCriteria.getLocation(),
                jobTypeStr,
                searchCriteria.getMinSalary(),
                searchCriteria.getRemote(),
                searchCriteria.getCompany(),
                pageable
        );
        
        // Debug: Show filtered jobs count
        System.out.println("Filtered jobs count: " + jobs.getTotalElements());
        
        return jobs.map(this::convertToDto);
    }

    @Override
    public JobDTO findById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
        return convertToDto(job);
    }

    @Override
    @Transactional
    public JobDTO createJob(JobDTO jobDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Employer employer = employerRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Current user is not an employer"));
        
        Job job = Job.builder()
                .title(jobDTO.getTitle())
                .description(jobDTO.getDescription())
                .company(jobDTO.getCompany())
                .location(jobDTO.getLocation())
                .jobType(jobDTO.getJobType())
                .salary(jobDTO.getSalary())
                .postedDate(LocalDateTime.now())
                .expirationDate(jobDTO.getExpirationDate())
                .requirements(jobDTO.getRequirements())
                .benefits(jobDTO.getBenefits())
                .employer(employer)
                .contactEmail(jobDTO.getContactEmail())
                .contactPhone(jobDTO.getContactPhone())
                .remote(jobDTO.isRemote())
                .active(true) // Explicitly set active to true
                .build();
                
        System.out.println("Creating job with active status: " + job.isActive()); // Debug
        
        Job savedJob = jobRepository.save(job);
        return convertToDto(savedJob);
    }

    @Override
    @Transactional
    public JobDTO updateJob(Long id, JobDTO jobDTO) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = employerRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Current user is not an employer"));
        
        if (!job.getEmployer().getId().equals(user.getId()) && 
                user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You are not authorized to update this job");
        }
        
        job.setTitle(jobDTO.getTitle());
        job.setDescription(jobDTO.getDescription());
        job.setCompany(jobDTO.getCompany());
        job.setLocation(jobDTO.getLocation());
        job.setJobType(jobDTO.getJobType());
        job.setSalary(jobDTO.getSalary());
        job.setExpirationDate(jobDTO.getExpirationDate());
        job.setRequirements(jobDTO.getRequirements());
        job.setBenefits(jobDTO.getBenefits());
        job.setContactEmail(jobDTO.getContactEmail());
        job.setContactPhone(jobDTO.getContactPhone());
        job.setRemote(jobDTO.isRemote());
        
        Job updatedJob = jobRepository.save(job);
        return convertToDto(updatedJob);
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = employerRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Current user is not an employer"));
        
        if (!job.getEmployer().getId().equals(user.getId()) && 
                user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You are not authorized to delete this job");
        }
        
        jobRepository.delete(job);
    }
    
    @Override
    public List<JobDTO> findAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job) {
        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .company(job.getCompany())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .salary(job.getSalary())
                .postedDate(job.getPostedDate())
                .expirationDate(job.getExpirationDate())
                .requirements(job.getRequirements())
                .benefits(job.getBenefits())
                .employerId(job.getEmployer().getId())
                .employerName(job.getEmployer().getName())
                .contactEmail(job.getContactEmail())
                .contactPhone(job.getContactPhone())
                .remote(job.isRemote())
                .build();
    }
}
