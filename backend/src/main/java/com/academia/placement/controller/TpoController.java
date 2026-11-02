package com.academia.placement.controller;

import com.academia.placement.dto.NaacReportDto;
import com.academia.placement.model.AuditLog;
import com.academia.placement.service.AuditService;
import com.academia.placement.service.TpoAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tpo")
@Validated
@Tag(name = "TPO Administration & Compliance", description = "Endpoints for accreditation analytics and immutable audit logs")
@SecurityRequirement(name = "BearerAuth")
public class TpoController {

    private final TpoAnalyticsService tpoAnalyticsService;
    private final AuditService auditService;

    public TpoController(TpoAnalyticsService tpoAnalyticsService, AuditService auditService) {
        this.tpoAnalyticsService = tpoAnalyticsService;
        this.auditService = auditService;
    }

    @GetMapping("/analytics/naac-summary")
    @PreAuthorize("hasRole('TPO_ADMIN')")
    @Operation(summary = "Generate NAAC Criterion 5 & NBA Criterion 4 placement metrics")
    public ResponseEntity<NaacReportDto> getNaacSummary(
            @RequestParam(defaultValue = "2026") int graduationYear) {
        return ResponseEntity.ok(tpoAnalyticsService.generateNaacSummary(graduationYear));
    }

    @GetMapping("/audits")
    @PreAuthorize("hasRole('TPO_ADMIN')")
    @Operation(summary = "Query immutable placement activity audit logs")
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(auditService.getAuditLogs(page, size));
    }
}

