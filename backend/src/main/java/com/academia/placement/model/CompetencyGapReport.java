package com.academia.placement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "competency_gap_reports")
public class CompetencyGapReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requisition_id", nullable = false)
    private JobRequisition requisition;

    @Column(nullable = false)
    private Integer missingSkillsCount = 0;

    @OneToMany(mappedBy = "gapReport", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CompetencyGapItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    public CompetencyGapReport() {}

    public CompetencyGapReport(StudentProfile student, JobRequisition requisition) {
        this.student = student;
        this.requisition = requisition;
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

    public JobRequisition getRequisition() {
        return requisition;
    }

    public void setRequisition(JobRequisition requisition) {
        this.requisition = requisition;
    }

    public Integer getMissingSkillsCount() {
        return missingSkillsCount;
    }

    public void setMissingSkillsCount(Integer missingSkillsCount) {
        this.missingSkillsCount = missingSkillsCount;
    }

    public List<CompetencyGapItem> getItems() {
        return items;
    }

    public void setItems(List<CompetencyGapItem> items) {
        this.items = items;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}

