package com.jobportal.dto;

import com.jobportal.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobSearchCriteria {

    private String keyword;
    private String location;
    private JobType jobType;
    private BigDecimal minSalary;
    private Boolean remote;
    private String company;
}
