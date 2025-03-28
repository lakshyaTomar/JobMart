package com.jobportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jobportal.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    @Positive(message = "Salary must be positive")
    private BigDecimal salary;

    private LocalDateTime postedDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    private String requirements;
    private String benefits;
    private Long employerId;
    private String employerName;
    private String contactEmail;
    private String contactPhone;
    private boolean remote;
}
