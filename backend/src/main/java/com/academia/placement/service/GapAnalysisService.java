package com.academia.placement.service;

import com.academia.placement.dto.CompetencyGapReportDto;
import com.academia.placement.dto.MissingSkillItemDto;
import com.academia.placement.model.*;
import com.academia.placement.repository.CourseCurriculumMappingRepository;
import com.academia.placement.repository.JobRequisitionRepository;
import com.academia.placement.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GapAnalysisService {

    private final StudentProfileRepository studentProfileRepository;
    private final JobRequisitionRepository jobRequisitionRepository;
    private final CourseCurriculumMappingRepository courseCurriculumMappingRepository;

    public GapAnalysisService(StudentProfileRepository studentProfileRepository,
                              JobRequisitionRepository jobRequisitionRepository,
                              CourseCurriculumMappingRepository courseCurriculumMappingRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.jobRequisitionRepository = jobRequisitionRepository;
        this.courseCurriculumMappingRepository = courseCurriculumMappingRepository;
    }

    @Transactional(readOnly = true)
    public CompetencyGapReportDto generateGapAnalysis(UUID studentId, UUID requisitionId) {
        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for id: " + studentId));

        JobRequisition requisition = jobRequisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Job requisition not found for id: " + requisitionId));

        return analyze(student, requisition);
    }

    public CompetencyGapReportDto analyze(StudentProfile student, JobRequisition requisition) {
        Set<UUID> studentSkillIds = student.getSkills().stream()
                .map(s -> s.getSkill().getId())
                .collect(Collectors.toSet());

        List<RequisitionSkill> reqSkills = requisition.getRequiredSkills() != null
                ? requisition.getRequiredSkills()
                : Collections.emptyList();

        List<MissingSkillItemDto> missingItems = new ArrayList<>();

        for (RequisitionSkill rs : reqSkills) {
            Skill skill = rs.getSkill();
            if (!studentSkillIds.contains(skill.getId())) {
                Optional<CourseCurriculumMapping> mappingOpt = courseCurriculumMappingRepository.findFirstBySkillId(skill.getId());

                String courseCode = mappingOpt.map(CourseCurriculumMapping::getCourseCode).orElse("CS-ELECTIVE");
                String courseName = mappingOpt.map(CourseCurriculumMapping::getCourseName).orElse("Core Department Elective");
                String remedial = mappingOpt.map(CourseCurriculumMapping::getRemedialGuide)
                        .orElse("Review laboratory exercises and official documentation for " + skill.getCanonicalName());

                missingItems.add(new MissingSkillItemDto(
                        skill.getId(),
                        skill.getCanonicalName(),
                        skill.getCategory() != null ? skill.getCategory().getName() : "General",
                        courseCode,
                        courseName,
                        remedial
                ));
            }
        }

        String companyName = requisition.getCompany() != null ? requisition.getCompany().getName() : "Enterprise Partner";

        return new CompetencyGapReportDto(
                student.getId(),
                requisition.getId(),
                requisition.getTitle(),
                companyName,
                missingItems.size(),
                missingItems
        );
    }
}

