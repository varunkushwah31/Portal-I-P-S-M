package com.academia.placement.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "course_curriculum_mappings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"skill_id", "course_code"})
})
public class CourseCurriculumMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false, length = 50)
    private String courseCode;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false, length = 50)
    private String department = "CS";

    @Column(columnDefinition = "TEXT")
    private String remedialGuide;

    public CourseCurriculumMapping() {}

    public CourseCurriculumMapping(Skill skill, String courseCode, String courseName, Integer semester, String department, String remedialGuide) {
        this.skill = skill;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
        this.department = department != null ? department : "CS";
        this.remedialGuide = remedialGuide;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRemedialGuide() {
        return remedialGuide;
    }

    public void setRemedialGuide(String remedialGuide) {
        this.remedialGuide = remedialGuide;
    }
}

