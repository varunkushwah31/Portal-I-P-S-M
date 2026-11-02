package com.academia.placement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobRequisitionDto {
    private UUID id;
    private String companyName;
    private String title;
    private String description;
    private BigDecimal minCgpa;
    private String jobType;
    private String status;
    private BigDecimal ctcLpa;
    private LocalDateTime deadline;
    private List<RequisitionSkillDto> requiredSkills = new ArrayList<>();

    public JobRequisitionDto() {}

    public JobRequisitionDto(UUID id, String companyName, String title, String description, BigDecimal minCgpa,
                             String jobType, String status, BigDecimal ctcLpa, LocalDateTime deadline,
                             List<RequisitionSkillDto> requiredSkills) {
        this.id = id;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.minCgpa = minCgpa;
        this.jobType = jobType;
        this.status = status;
        this.ctcLpa = ctcLpa;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getMinCgpa() { return minCgpa; }
    public void setMinCgpa(BigDecimal minCgpa) { this.minCgpa = minCgpa; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getCtcLpa() { return ctcLpa; }
    public void setCtcLpa(BigDecimal ctcLpa) { this.ctcLpa = ctcLpa; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public List<RequisitionSkillDto> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<RequisitionSkillDto> requiredSkills) { this.requiredSkills = requiredSkills; }
}

