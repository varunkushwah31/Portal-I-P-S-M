package com.academia.placement.controller;

import com.academia.placement.dto.CompatibilityReportDto;
import com.academia.placement.dto.CompetencyGapReportDto;
import com.academia.placement.model.Application;
import com.academia.placement.model.JobRequisition;
import com.academia.placement.model.StudentProfile;
import com.academia.placement.repository.ApplicationRepository;
import com.academia.placement.repository.JobRequisitionRepository;
import com.academia.placement.repository.StudentProfileRepository;
import com.academia.placement.service.AuditService;
import com.academia.placement.service.GapAnalysisService;
import com.academia.placement.service.MatchingEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matching")
@Tag(name = "Deterministic Matching & Gap Analysis", description = "Algorithmic Jaccard/Cosine engine and competency gap reporter")
@SecurityRequirement(name = "BearerAuth")
public class MatchingController {

    private final MatchingEngineService matchingEngineService;
    private final GapAnalysisService gapAnalysisService;
    private final StudentProfileRepository studentProfileRepository;
    private final JobRequisitionRepository jobRequisitionRepository;
    private final ApplicationRepository applicationRepository;
    private final AuditService auditService;

    public MatchingController(MatchingEngineService matchingEngineService,
                              GapAnalysisService gapAnalysisService,
                              StudentProfileRepository studentProfileRepository,
                              JobRequisitionRepository jobRequisitionRepository,
                              ApplicationRepository applicationRepository,
                              AuditService auditService) {
        this.matchingEngineService = matchingEngineService;
        this.gapAnalysisService = gapAnalysisService;
        this.studentProfileRepository = studentProfileRepository;
        this.jobRequisitionRepository = jobRequisitionRepository;
        this.applicationRepository = applicationRepository;
        this.auditService = auditService;
    }

    @GetMapping("/compatibility")
    @PreAuthorize("hasAnyRole('STUDENT', 'RECRUITER', 'TPO_ADMIN')")
    @Operation(summary = "Compute deterministic Jaccard + Cosine compatibility score")
    public ResponseEntity<CompatibilityReportDto> getCompatibility(
            @RequestParam UUID studentId,
            @RequestParam UUID requisitionId) {
        return ResponseEntity.ok(matchingEngineService.calculateCompatibility(studentId, requisitionId));
    }

    @GetMapping("/gap-analysis")
    @PreAuthorize("hasAnyRole('STUDENT', 'RECRUITER', 'TPO_ADMIN')")
    @Operation(summary = "Generate diagnostic competency gap report with curriculum course mappings")
    public ResponseEntity<CompetencyGapReportDto> getGapAnalysis(
            @RequestParam UUID studentId,
            @RequestParam UUID requisitionId) {
        return ResponseEntity.ok(gapAnalysisService.generateGapAnalysis(studentId, requisitionId));
    }

    @PostMapping("/apply")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Submit application for job requisition with deterministic eligibility check")
    public ResponseEntity<Application> applyToRequisition(@RequestParam UUID requisitionId,
                                                          Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        StudentProfile student = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user: " + userId));

        JobRequisition requisition = jobRequisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Requisition not found: " + requisitionId));

        if (applicationRepository.existsByRequisitionIdAndStudentId(requisitionId, student.getId())) {
            throw new IllegalArgumentException("Application has already been submitted for this requisition.");
        }

        CompatibilityReportDto compatibility = matchingEngineService.computeScore(student, requisition);

        Application application = new Application(requisition, student, compatibility.getCompositeScore());
        application = applicationRepository.save(application);

        auditService.recordEvent(
                userId,
                "STUDENT",
                "APPLICATION_SUBMITTED",
                "Application",
                application.getId(),
                "Applied to requisition: " + requisition.getTitle() + " with score: " + compatibility.getCompositeScore(),
                "0.0.0.0"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }
}

