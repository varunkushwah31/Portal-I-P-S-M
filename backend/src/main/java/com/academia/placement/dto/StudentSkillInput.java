package com.academia.placement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class StudentSkillInput {

    @NotNull
    private UUID skillId;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private BigDecimal proficiencyLevel;

    public StudentSkillInput() {}

    public StudentSkillInput(UUID skillId, BigDecimal proficiencyLevel) {
        this.skillId = skillId;
        this.proficiencyLevel = proficiencyLevel;
    }

    public UUID getSkillId() { return skillId; }
    public void setSkillId(UUID skillId) { this.skillId = skillId; }

    public BigDecimal getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(BigDecimal proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }
}

