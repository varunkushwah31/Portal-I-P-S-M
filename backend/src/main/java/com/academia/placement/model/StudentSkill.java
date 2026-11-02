package com.academia.placement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "skill_id"})
})
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal proficiencyLevel; // 0.0 to 1.0

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(length = 100)
    private String verificationSource = "SELF_REPORTED";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public StudentSkill() {}

    public StudentSkill(StudentProfile student, Skill skill, BigDecimal proficiencyLevel, boolean isVerified, String verificationSource) {
        this.student = student;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel;
        this.isVerified = isVerified;
        this.verificationSource = verificationSource;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StudentProfile getStudent() {
        return student;
    }

    public void setStudent(StudentProfile student) {
        this.student = student;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public BigDecimal getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(BigDecimal proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getVerificationSource() {
        return verificationSource;
    }

    public void setVerificationSource(String verificationSource) {
        this.verificationSource = verificationSource;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

