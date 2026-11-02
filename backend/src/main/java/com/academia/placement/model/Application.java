package com.academia.placement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"requisition_id", "student_id"})
})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requisition_id", nullable = false)
    private JobRequisition requisition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal compatibilityScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationStage currentStage = ApplicationStage.APPLIED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Application() {}

    public Application(JobRequisition requisition, StudentProfile student, BigDecimal compatibilityScore) {
        this.requisition = requisition;
        this.student = student;
        this.compatibilityScore = compatibilityScore;
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

    public StudentProfile getStudent() {
        return student;
    }

    public void setStudent(StudentProfile student) {
        this.student = student;
    }

    public BigDecimal getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(BigDecimal compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public ApplicationStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(ApplicationStage currentStage) {
        this.currentStage = currentStage;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

