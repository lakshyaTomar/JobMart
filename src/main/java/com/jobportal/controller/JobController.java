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
        
        JobSearchCriteria searchCriteria = new JobSearchCriteria();
        searchCriteria.setKeyword(keyword);
        searchCriteria.setLocation(location);
        searchCriteria.setMinSalary(minSalary);
        searchCriteria.setRemote(remote);
        searchCriteria.setCompany(company);
        
        // Safely convert the jobType string to enum
        if (jobType != null && !jobType.isEmpty()) {
            try {
                searchCriteria.setJobType(com.jobportal.enums.JobType.valueOf(jobType));
            } catch (IllegalArgumentException e) {
                // Log the error but continue with null jobType
                System.out.println("Invalid job type: " + jobType);
            }
        }
        
        return ResponseEntity.ok(jobService.findJobs(searchCriteria, pageable));
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
