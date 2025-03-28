package com.jobportal.service;

import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface JobService {
    
    Page<JobDTO> findJobs(JobSearchCriteria searchCriteria, Pageable pageable);
    
    JobDTO findById(Long id);
    
    JobDTO createJob(JobDTO jobDTO);
    
    JobDTO updateJob(Long id, JobDTO jobDTO);
    
    void deleteJob(Long id);
    
    /**
     * Find all jobs in the system
     * @return List of all jobs
     */
    List<JobDTO> findAllJobs();
}
