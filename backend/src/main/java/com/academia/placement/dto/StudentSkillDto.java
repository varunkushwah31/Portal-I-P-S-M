package com.academia.placement.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class StudentSkillDto {
    private UUID skillId;
    private String skillName;
    private String category;
    private BigDecimal proficiencyLevel;
    private boolean isVerified;
    private String verificationSource;

    public StudentSkillDto() {}

    public StudentSkillDto(UUID skillId, String skillName, String category, BigDecimal proficiencyLevel, boolean isVerified, String verificationSource) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
        this.isVerified = isVerified;
        this.verificationSource = verificationSource;
    }

    public UUID getSkillId() { return skillId; }
    public void setSkillId(UUID skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(BigDecimal proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getVerificationSource() { return verificationSource; }
    public void setVerificationSource(String verificationSource) { this.verificationSource = verificationSource; }
}

