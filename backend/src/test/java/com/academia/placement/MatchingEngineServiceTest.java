package com.academia.placement;

import com.academia.placement.dto.CompatibilityReportDto;
import com.academia.placement.model.*;
import com.academia.placement.repository.JobRequisitionRepository;
import com.academia.placement.repository.StudentProfileRepository;
import com.academia.placement.service.MatchingEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchingEngineServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobRequisitionRepository jobRequisitionRepository;

    @InjectMocks
    private MatchingEngineService matchingEngineService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(matchingEngineService, "jaccardWeight", 0.40);
        ReflectionTestUtils.setField(matchingEngineService, "cosineWeight", 0.60);
        ReflectionTestUtils.setField(matchingEngineService, "eligibilityThreshold", 0.50);
    }

    @Test
    @DisplayName("Calculate deterministic compatibility score between student and requisition")
    void testComputeScoreDeterministic() {
        // Given
        SkillCategory cat = new SkillCategory("Core CS", "Core Computer Science");
        cat.setId(UUID.randomUUID());

        Skill skill1 = new Skill("DSA", cat, "Algorithms");
        skill1.setId(UUID.randomUUID());

        Skill skill2 = new Skill("Spring Boot", cat, "Framework");
        skill2.setId(UUID.randomUUID());

        Skill skill3 = new Skill("PostgreSQL", cat, "Database");
        skill3.setId(UUID.randomUUID());

        // Student has skill1 (0.8) and skill2 (0.7)
        StudentProfile student = new StudentProfile(
                null, "2100290120001", "Ayush Sharma", "CS", BigDecimal.valueOf(8.5), 2026
        );
        student.setId(UUID.randomUUID());

        StudentSkill ss1 = new StudentSkill(student, skill1, BigDecimal.valueOf(0.80), true, "VERIFIED");
        StudentSkill ss2 = new StudentSkill(student, skill2, BigDecimal.valueOf(0.70), true, "VERIFIED");
        student.setSkills(List.of(ss1, ss2));

        // Job requires skill1 (wt=5, minProf=0.8), skill2 (wt=4, minProf=0.6), skill3 (wt=3, minProf=0.5)
        JobRequisition req = new JobRequisition(
                null, null, "SDE-1", "Backend Role", BigDecimal.valueOf(7.0), "FULL_TIME",
                BigDecimal.valueOf(12.0), LocalDateTime.now().plusDays(10)
        );
        req.setId(UUID.randomUUID());

        RequisitionSkill rs1 = new RequisitionSkill(req, skill1, 5, BigDecimal.valueOf(0.80));
        RequisitionSkill rs2 = new RequisitionSkill(req, skill2, 4, BigDecimal.valueOf(0.60));
        RequisitionSkill rs3 = new RequisitionSkill(req, skill3, 3, BigDecimal.valueOf(0.50));
        req.setRequiredSkills(List.of(rs1, rs2, rs3));

        // When
        CompatibilityReportDto report = matchingEngineService.computeScore(student, req);

        // Then
        assertNotNull(report);
        assertEquals(2, report.getMatchedSkillsCount());
        assertEquals(3, report.getTotalRequiredSkillsCount());

        // Jaccard: |Intersection| / |Union| = 2 / 3 = 0.6667
        assertEquals(0.6667, report.getJaccardIndex().doubleValue(), 0.001);

        // Cosine should be strictly positive and <= 1.0
        assertTrue(report.getCosineSimilarity().doubleValue() > 0.0);
        assertTrue(report.getCosineSimilarity().doubleValue() <= 1.0);

        // Composite should be >= 0.50, thus eligible
        assertTrue(report.getCompositeScore().doubleValue() >= 0.50);
        assertTrue(report.isEligible());
    }

    @Test
    @DisplayName("Candidate is ineligible if CGPA is below minimum threshold")
    void testIneligibleDueToCgpa() {
        SkillCategory cat = new SkillCategory("Core CS", "Core Computer Science");
        cat.setId(UUID.randomUUID());
        Skill skill = new Skill("DSA", cat, "Algorithms");
        skill.setId(UUID.randomUUID());

        StudentProfile student = new StudentProfile(
                null, "2100290120002", "Candidate", "CS", BigDecimal.valueOf(6.2), 2026
        );
        student.setId(UUID.randomUUID());
        student.setSkills(List.of(new StudentSkill(student, skill, BigDecimal.valueOf(0.9), true, "VERIFIED")));

        JobRequisition req = new JobRequisition(
                null, null, "SDE-1", "Role", BigDecimal.valueOf(7.5), "FULL_TIME",
                BigDecimal.valueOf(10.0), LocalDateTime.now().plusDays(10)
        );
        req.setId(UUID.randomUUID());
        req.setRequiredSkills(List.of(new RequisitionSkill(req, skill, 5, BigDecimal.valueOf(0.8))));

        CompatibilityReportDto report = matchingEngineService.computeScore(student, req);

        assertNotNull(report);
        assertFalse(report.isEligible(), "Candidate should be flagged ineligible due to CGPA deficiency");
    }
}

