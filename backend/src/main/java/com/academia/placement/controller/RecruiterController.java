package com.academia.placement.controller;

import com.academia.placement.dto.ApplicationStatusUpdateRequest;
import com.academia.placement.dto.CreateRequisitionRequest;
import com.academia.placement.dto.JobRequisitionDto;
import com.academia.placement.service.RecruiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recruiter")
@Validated
@Tag(name = "Recruiter Operations", description = "Endpoints for posting job requisitions and candidate progression")
@SecurityRequirement(name = "BearerAuth")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @PostMapping("/requisitions")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Create and publish a new job requisition")
    public ResponseEntity<JobRequisitionDto> createRequisition(@Valid @RequestBody CreateRequisitionRequest request,
                                                               Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        JobRequisitionDto created = recruiterService.createRequisition(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/requisitions")
    @PreAuthorize("hasAnyRole('RECRUITER', 'STUDENT', 'TPO_ADMIN')")
    @Operation(summary = "List all active job requisitions")
    public ResponseEntity<List<JobRequisitionDto>> getOpenRequisitions() {
        return ResponseEntity.ok(recruiterService.getAllOpenRequisitions());
    }

    @GetMapping("/requisitions/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'STUDENT', 'TPO_ADMIN')")
    @Operation(summary = "Get requisition details by ID")
    public ResponseEntity<JobRequisitionDto> getRequisitionById(@PathVariable UUID id) {
        return ResponseEntity.ok(recruiterService.getRequisitionById(id));
    }

    @PatchMapping("/applications/{applicationId}/stage")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Advance or update candidate stage in recruitment pipeline")
    public ResponseEntity<Void> updateApplicationStage(@PathVariable UUID applicationId,
                                                       @Valid @RequestBody ApplicationStatusUpdateRequest request,
                                                       Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        recruiterService.updateApplicationStage(applicationId, request.getStage(), userId);
        return ResponseEntity.noContent().build();
    }
}

