package com.academia.placement.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "competency_gap_items")
public class CompetencyGapItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gap_report_id", nullable = false)
    private CompetencyGapReport gapReport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    private String courseCode;

    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String remedialRecommendation;

    public CompetencyGapItem() {}

    public CompetencyGapItem(CompetencyGapReport gapReport, Skill skill, String courseCode, String courseName, String remedialRecommendation) {
        this.gapReport = gapReport;
        this.skill = skill;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.remedialRecommendation = remedialRecommendation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CompetencyGapReport getGapReport() {
        return gapReport;
    }

    public void setGapReport(CompetencyGapReport gapReport) {
        this.gapReport = gapReport;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRemedialRecommendation() {
        return remedialRecommendation;
    }

    public void setRemedialRecommendation(String remedialRecommendation) {
        this.remedialRecommendation = remedialRecommendation;
    }
}

