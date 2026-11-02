package com.academia.placement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreateRequisitionRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal minCgpa;

    @NotBlank
    private String jobType; // INTERNSHIP or FULL_TIME

    private BigDecimal ctcLpa;

    @NotNull
    private LocalDateTime deadline;

    @NotEmpty
    private List<RequisitionSkillInput> skills;

    public static class RequisitionSkillInput {
        @NotNull
        private UUID skillId;

        @NotNull
        private Integer weight; // 1 to 5

        @NotNull
        private BigDecimal minProficiency;

        public RequisitionSkillInput() {}

        public RequisitionSkillInput(UUID skillId, Integer weight, BigDecimal minProficiency) {
            this.skillId = skillId;
            this.weight = weight;
            this.minProficiency = minProficiency;
        }

        public UUID getSkillId() { return skillId; }
        public void setSkillId(UUID skillId) { this.skillId = skillId; }

        public Integer getWeight() { return weight; }
        public void setWeight(Integer weight) { this.weight = weight; }

        public BigDecimal getMinProficiency() { return minProficiency; }
        public void setMinProficiency(BigDecimal minProficiency) { this.minProficiency = minProficiency; }
    }

    public CreateRequisitionRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getMinCgpa() { return minCgpa; }
    public void setMinCgpa(BigDecimal minCgpa) { this.minCgpa = minCgpa; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public BigDecimal getCtcLpa() { return ctcLpa; }
    public void setCtcLpa(BigDecimal ctcLpa) { this.ctcLpa = ctcLpa; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public List<RequisitionSkillInput> getSkills() { return skills; }
    public void setSkills(List<RequisitionSkillInput> skills) { this.skills = skills; }
}

