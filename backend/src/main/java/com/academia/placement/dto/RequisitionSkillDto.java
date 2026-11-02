package com.academia.placement.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class RequisitionSkillDto {
    private UUID skillId;
    private String skillName;
    private Integer weight;
    private BigDecimal minProficiency;

    public RequisitionSkillDto() {}

    public RequisitionSkillDto(UUID skillId, String skillName, Integer weight, BigDecimal minProficiency) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.weight = weight;
        this.minProficiency = minProficiency;
    }

    public UUID getSkillId() { return skillId; }
    public void setSkillId(UUID skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public BigDecimal getMinProficiency() { return minProficiency; }
    public void setMinProficiency(BigDecimal minProficiency) { this.minProficiency = minProficiency; }
}

