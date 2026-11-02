package com.academia.placement;

import com.academia.placement.dto.CompetencyGapReportDto;
import com.academia.placement.model.*;
import com.academia.placement.repository.CourseCurriculumMappingRepository;
import com.academia.placement.repository.JobRequisitionRepository;
import com.academia.placement.repository.StudentProfileRepository;
import com.academia.placement.service.GapAnalysisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GapAnalysisServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobRequisitionRepository jobRequisitionRepository;

    @Mock
    private CourseCurriculumMappingRepository courseCurriculumMappingRepository;

    @InjectMocks
    private GapAnalysisService gapAnalysisService;

    @Test
    @DisplayName("Derive missing skills and attach curriculum remediation guidance")
    void testCompetencyGapDerivation() {
        SkillCategory cat = new SkillCategory("Storage", "Databases");
        cat.setId(UUID.randomUUID());

        Skill skill1 = new Skill("DSA", cat, "Algorithms");
        skill1.setId(UUID.randomUUID());

        Skill missingSkill = new Skill("PostgreSQL", cat, "Relational DB");
        missingSkill.setId(UUID.randomUUID());

        StudentProfile student = new StudentProfile(
                null, "2100290120001", "Ayush", "CS", BigDecimal.valueOf(8.5), 2026
        );
        student.setId(UUID.randomUUID());

        StudentSkill ss1 = new StudentSkill(student, skill1, BigDecimal.valueOf(0.85), true, "VERIFIED");
        student.setSkills(List.of(ss1));

        JobRequisition req = new JobRequisition(
                null, null, "Database Engineer", "Desc", BigDecimal.valueOf(6.5), "FULL_TIME",
                BigDecimal.valueOf(14.0), LocalDateTime.now().plusDays(10)
        );
        req.setId(UUID.randomUUID());

        RequisitionSkill rs1 = new RequisitionSkill(req, skill1, 5, BigDecimal.valueOf(0.80));
        RequisitionSkill rs2 = new RequisitionSkill(req, missingSkill, 4, BigDecimal.valueOf(0.70));
        req.setRequiredSkills(List.of(rs1, rs2));

        CourseCurriculumMapping mapping = new CourseCurriculumMapping(
                missingSkill, "CS-501", "Database Management Systems", 5, "CS", "Review 3NF Normalization"
        );
        when(courseCurriculumMappingRepository.findFirstBySkillId(missingSkill.getId())).thenReturn(Optional.of(mapping));

        // When
        CompetencyGapReportDto report = gapAnalysisService.analyze(student, req);

        // Then
        assertNotNull(report);
        assertEquals(1, report.getMissingSkillsCount());
        assertEquals("PostgreSQL", report.getMissingSkills().get(0).getSkillName());
        assertEquals("CS-501", report.getMissingSkills().get(0).getCourseCode());
        assertEquals("Database Management Systems", report.getMissingSkills().get(0).getCourseName());
    }
}

