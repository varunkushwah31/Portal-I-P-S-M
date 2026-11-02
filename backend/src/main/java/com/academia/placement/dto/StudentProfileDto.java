package com.academia.placement.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentProfileDto {
    private UUID id;
    private UUID userId;
    private String rollNumber;
    private String fullName;
    private String department;
    private BigDecimal cgpa;
    private Integer graduationYear;
    private boolean isPlaced;
    private List<StudentSkillDto> skills = new ArrayList<>();

    public StudentProfileDto() {}

    public StudentProfileDto(UUID id, UUID userId, String rollNumber, String fullName, String department,
                             BigDecimal cgpa, Integer graduationYear, boolean isPlaced, List<StudentSkillDto> skills) {
        this.id = id;
        this.userId = userId;
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.department = department;
        this.cgpa = cgpa;
        this.graduationYear = graduationYear;
        this.isPlaced = isPlaced;
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public BigDecimal getCgpa() { return cgpa; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public boolean isPlaced() { return isPlaced; }
    public void setPlaced(boolean placed) { isPlaced = placed; }

    public List<StudentSkillDto> getSkills() { return skills; }
    public void setSkills(List<StudentSkillDto> skills) { this.skills = skills; }
}

