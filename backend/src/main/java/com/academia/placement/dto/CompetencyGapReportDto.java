package com.academia.placement.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompetencyGapReportDto {
    private UUID studentId;
    private UUID requisitionId;
    private String jobTitle;
    private String companyName;
    private int missingSkillsCount;
    private List<MissingSkillItemDto> missingSkills = new ArrayList<>();

    public CompetencyGapReportDto() {}

    public CompetencyGapReportDto(UUID studentId, UUID requisitionId, String jobTitle, String companyName,
                                  int missingSkillsCount, List<MissingSkillItemDto> missingSkills) {
        this.studentId = studentId;
        this.requisitionId = requisitionId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.missingSkillsCount = missingSkillsCount;
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
    }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public UUID getRequisitionId() { return requisitionId; }
    public void setRequisitionId(UUID requisitionId) { this.requisitionId = requisitionId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public int getMissingSkillsCount() { return missingSkillsCount; }
    public void setMissingSkillsCount(int missingSkillsCount) { this.missingSkillsCount = missingSkillsCount; }

    public List<MissingSkillItemDto> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<MissingSkillItemDto> missingSkills) { this.missingSkills = missingSkills; }
}

