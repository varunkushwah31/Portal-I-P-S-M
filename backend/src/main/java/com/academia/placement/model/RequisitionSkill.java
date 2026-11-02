package com.academia.placement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "requisition_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"requisition_id", "skill_id"})
})
public class RequisitionSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_id", nullable = false)
    private JobRequisition requisition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Integer weight; // 1 (Optional) to 5 (Mandatory)

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal minProficiency = BigDecimal.valueOf(0.50);

    public RequisitionSkill() {}

    public RequisitionSkill(JobRequisition requisition, Skill skill, Integer weight, BigDecimal minProficiency) {
        this.requisition = requisition;
        this.skill = skill;
        this.weight = weight;
        this.minProficiency = minProficiency != null ? minProficiency : BigDecimal.valueOf(0.50);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public JobRequisition getRequisition() {
        return requisition;
    }

    public void setRequisition(JobRequisition requisition) {
        this.requisition = requisition;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public BigDecimal getMinProficiency() {
        return minProficiency;
    }

    public void setMinProficiency(BigDecimal minProficiency) {
        this.minProficiency = minProficiency;
    }
}

