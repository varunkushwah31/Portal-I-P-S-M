package com.academia.placement.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NaacReportDto {
    private String academicYear;
    private long totalGraduatingStudents;
    private long totalPlacedStudents;
    private BigDecimal placementPercentage;
    private BigDecimal averagePackageLpa;
    private long totalCorporateDrives;
    private List<String> topDeficitSkills = new ArrayList<>();

    public NaacReportDto() {}

    public NaacReportDto(String academicYear, long totalGraduatingStudents, long totalPlacedStudents,
                         BigDecimal placementPercentage, BigDecimal averagePackageLpa,
                         long totalCorporateDrives, List<String> topDeficitSkills) {
        this.academicYear = academicYear;
        this.totalGraduatingStudents = totalGraduatingStudents;
        this.totalPlacedStudents = totalPlacedStudents;
        this.placementPercentage = placementPercentage;
        this.averagePackageLpa = averagePackageLpa;
        this.totalCorporateDrives = totalCorporateDrives;
        this.topDeficitSkills = topDeficitSkills != null ? topDeficitSkills : new ArrayList<>();
    }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public long getTotalGraduatingStudents() { return totalGraduatingStudents; }
    public void setTotalGraduatingStudents(long totalGraduatingStudents) { this.totalGraduatingStudents = totalGraduatingStudents; }

    public long getTotalPlacedStudents() { return totalPlacedStudents; }
    public void setTotalPlacedStudents(long totalPlacedStudents) { this.totalPlacedStudents = totalPlacedStudents; }

    public BigDecimal getPlacementPercentage() { return placementPercentage; }
    public void setPlacementPercentage(BigDecimal placementPercentage) { this.placementPercentage = placementPercentage; }

    public BigDecimal getAveragePackageLpa() { return averagePackageLpa; }
    public void setAveragePackageLpa(BigDecimal averagePackageLpa) { this.averagePackageLpa = averagePackageLpa; }

    public long getTotalCorporateDrives() { return totalCorporateDrives; }
    public void setTotalCorporateDrives(long totalCorporateDrives) { this.totalCorporateDrives = totalCorporateDrives; }

    public List<String> getTopDeficitSkills() { return topDeficitSkills; }
    public void setTopDeficitSkills(List<String> topDeficitSkills) { this.topDeficitSkills = topDeficitSkills; }
}

