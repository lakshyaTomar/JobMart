package com.jobportal.controller;

import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobSearchCriteria;
import com.jobportal.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job management API")
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "Get all jobs", description = "Retrieve all jobs with pagination and filtering")
    public ResponseEntity<Page<JobDTO>> getAllJobs(
            JobSearchCriteria searchCriteria,
            Pageable pageable) {
        return ResponseEntity.ok(jobService.findJobs(searchCriteria, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieve a specific job by its ID")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYER')")
    @Operation(summary = "Create job", description = "Create a new job listing (employers only)")
    public ResponseEntity<JobDTO> createJob(@Valid @RequestBody JobDTO jobDTO) {
        return new ResponseEntity<>(jobService.createJob(jobDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    @Operation(summary = "Update job", description = "Update an existing job listing (employers only)")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @Valid @RequestBody JobDTO jobDTO) {
        return ResponseEntity.ok(jobService.updateJob(id, jobDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    @Operation(summary = "Delete job", description = "Delete a job listing (employers only)")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
