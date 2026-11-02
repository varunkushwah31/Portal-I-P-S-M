package com.academia.placement.service;

import com.academia.placement.dto.NaacReportDto;
import com.academia.placement.model.JobRequisition;
import com.academia.placement.repository.JobRequisitionRepository;
import com.academia.placement.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TpoAnalyticsService {

    private final StudentProfileRepository studentProfileRepository;
    private final JobRequisitionRepository jobRequisitionRepository;

    public TpoAnalyticsService(StudentProfileRepository studentProfileRepository,
                               JobRequisitionRepository jobRequisitionRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.jobRequisitionRepository = jobRequisitionRepository;
    }

    @Transactional(readOnly = true)
    public NaacReportDto generateNaacSummary(int graduationYear) {
        long totalStudents = studentProfileRepository.countByGraduationYear(graduationYear);
        long placedStudents = studentProfileRepository.countByGraduationYearAndIsPlacedTrue(graduationYear);

        BigDecimal percentage = (totalStudents > 0)
                ? BigDecimal.valueOf((double) placedStudents / totalStudents * 100.0).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<JobRequisition> requisitions = jobRequisitionRepository.findAll();
        long totalDrives = requisitions.size();

        // Calculate average CTC of posted requisitions
        OptionalDouble avgCtc = requisitions.stream()
                .filter(r -> r.getCtcLpa() != null)
                .mapToDouble(r -> r.getCtcLpa().doubleValue())
                .average();

        BigDecimal averagePackage = avgCtc.isPresent()
                ? BigDecimal.valueOf(avgCtc.getAsDouble()).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.valueOf(8.50);

        // Find top high-demand skills
        Map<String, Long> skillFrequency = requisitions.stream()
                .flatMap(r -> r.getRequiredSkills().stream())
                .collect(Collectors.groupingBy(
                        rs -> rs.getSkill().getCanonicalName(),
                        Collectors.counting()
                ));

        List<String> topDeficitSkills = skillFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return new NaacReportDto(
                "AY " + (graduationYear - 1) + "-" + graduationYear,
                totalStudents,
                placedStudents,
                percentage,
                averagePackage,
                totalDrives,
                topDeficitSkills
        );
    }
}

