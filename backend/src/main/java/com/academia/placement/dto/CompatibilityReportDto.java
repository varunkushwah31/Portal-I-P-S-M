package com.academia.placement.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CompatibilityReportDto {
    private UUID studentId;
    private UUID requisitionId;
    private String jobTitle;
    private BigDecimal jaccardIndex;
    private BigDecimal cosineSimilarity;
    private BigDecimal compositeScore;
    private int matchedSkillsCount;
    private int totalRequiredSkillsCount;
    private boolean isEligible;

    public CompatibilityReportDto() {}

    public CompatibilityReportDto(UUID studentId, UUID requisitionId, String jobTitle, BigDecimal jaccardIndex,
                                  BigDecimal cosineSimilarity, BigDecimal compositeScore,
                                  int matchedSkillsCount, int totalRequiredSkillsCount, boolean isEligible) {
        this.studentId = studentId;
        this.requisitionId = requisitionId;
        this.jobTitle = jobTitle;
        this.jaccardIndex = jaccardIndex;
        this.cosineSimilarity = cosineSimilarity;
        this.compositeScore = compositeScore;
        this.matchedSkillsCount = matchedSkillsCount;
        this.totalRequiredSkillsCount = totalRequiredSkillsCount;
        this.isEligible = isEligible;
    }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public UUID getRequisitionId() { return requisitionId; }
    public void setRequisitionId(UUID requisitionId) { this.requisitionId = requisitionId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public BigDecimal getJaccardIndex() { return jaccardIndex; }
    public void setJaccardIndex(BigDecimal jaccardIndex) { this.jaccardIndex = jaccardIndex; }

    public BigDecimal getCosineSimilarity() { return cosineSimilarity; }
    public void setCosineSimilarity(BigDecimal cosineSimilarity) { this.cosineSimilarity = cosineSimilarity; }

    public BigDecimal getCompositeScore() { return compositeScore; }
    public void setCompositeScore(BigDecimal compositeScore) { this.compositeScore = compositeScore; }

    public int getMatchedSkillsCount() { return matchedSkillsCount; }
    public void setMatchedSkillsCount(int matchedSkillsCount) { this.matchedSkillsCount = matchedSkillsCount; }

    public int getTotalRequiredSkillsCount() { return totalRequiredSkillsCount; }
    public void setTotalRequiredSkillsCount(int totalRequiredSkillsCount) { this.totalRequiredSkillsCount = totalRequiredSkillsCount; }

    public boolean isEligible() { return isEligible; }
    public void setEligible(boolean eligible) { isEligible = eligible; }
}

