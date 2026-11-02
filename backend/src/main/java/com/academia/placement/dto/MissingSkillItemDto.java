package com.academia.placement.dto;

import java.util.UUID;

public class MissingSkillItemDto {
    private UUID skillId;
    private String skillName;
    private String category;
    private String courseCode;
    private String courseName;
    private String remedialGuide;

    public MissingSkillItemDto() {}

    public MissingSkillItemDto(UUID skillId, String skillName, String category, String courseCode, String courseName, String remedialGuide) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.remedialGuide = remedialGuide;
    }

    public UUID getSkillId() { return skillId; }
    public void setSkillId(UUID skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getRemedialGuide() { return remedialGuide; }
    public void setRemedialGuide(String remedialGuide) { this.remedialGuide = remedialGuide; }
}

