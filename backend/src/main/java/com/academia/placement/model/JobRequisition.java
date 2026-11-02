package com.academia.placement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_requisitions")
public class JobRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private RecruiterProfile recruiter;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal minCgpa = BigDecimal.valueOf(6.0);

    @Column(nullable = false)
    private String jobType; // INTERNSHIP or FULL_TIME

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, CLOSED, DRAFT

    @Column(precision = 6, scale = 2)
    private BigDecimal ctcLpa;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RequisitionSkill> requiredSkills = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public JobRequisition() {}

    public JobRequisition(Company company, RecruiterProfile recruiter, String title, String description,
                          BigDecimal minCgpa, String jobType, BigDecimal ctcLpa, LocalDateTime deadline) {
        this.company = company;
        this.recruiter = recruiter;
        this.title = title;
        this.description = description;
        this.minCgpa = minCgpa;
        this.jobType = jobType;
        this.ctcLpa = ctcLpa;
        this.deadline = deadline;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public RecruiterProfile getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(RecruiterProfile recruiter) {
        this.recruiter = recruiter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(BigDecimal minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getCtcLpa() {
        return ctcLpa;
    }

    public void setCtcLpa(BigDecimal ctcLpa) {
        this.ctcLpa = ctcLpa;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public List<RequisitionSkill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<RequisitionSkill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

