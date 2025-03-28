package com.jobportal.controller;

import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobSearchCriteria;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/search")
    public ResponseEntity<Page<JobDTO>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) Boolean remote,
            @RequestParam(required = false) String company,
            Pageable pageable) {
        
        // Create a map of parameters for debugging
        System.out.println("Search parameters received:");
        System.out.println("keyword: " + keyword);
        System.out.println("location: " + location);
        System.out.println("jobType: " + jobType);
        System.out.println("minSalary: " + minSalary);
        System.out.println("remote: " + remote);
        System.out.println("company: " + company);
        
        // Create criteria without builder to avoid any conversion issues
        JobSearchCriteria searchCriteria = new JobSearchCriteria();
        searchCriteria.setKeyword(keyword);
        searchCriteria.setLocation(location);
        searchCriteria.setMinSalary(minSalary);
        searchCriteria.setRemote(remote);
        searchCriteria.setCompany(company);
        
        // Handle jobType separately with clear error handling
        if (jobType != null && !jobType.isEmpty()) {
            try {
                // Try converting string to enum using exact case matching
                com.jobportal.enums.JobType enumJobType = com.jobportal.enums.JobType.valueOf(jobType);
                searchCriteria.setJobType(enumJobType);
                System.out.println("Successfully converted jobType to enum: " + enumJobType);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid jobType value: " + jobType + ". Error: " + e.getMessage());
                // Leave jobType as null in searchCriteria
            }
        }
        
        // Get all jobs from database for debugging
        System.out.println("All jobs in database:");
        jobService.findAllJobs().forEach(job -> 
            System.out.println("Job: " + job.getTitle() + ", Type: " + job.getJobType()));
        
        // Proceed with search
        Page<JobDTO> results = jobService.findJobs(searchCriteria, pageable);
        System.out.println("Search returned " + results.getTotalElements() + " results");
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping
    public ResponseEntity<Page<JobDTO>> getAllJobs(
            JobSearchCriteria searchCriteria,
            Pageable pageable) {
        return ResponseEntity.ok(jobService.findJobs(searchCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<JobDTO> createJob(@Valid @RequestBody JobDTO jobDTO) {
        return new ResponseEntity<>(jobService.createJob(jobDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @Valid @RequestBody JobDTO jobDTO) {
        return ResponseEntity.ok(jobService.updateJob(id, jobDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
