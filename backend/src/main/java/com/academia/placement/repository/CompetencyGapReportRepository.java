package com.academia.placement.repository;

import com.academia.placement.model.CompetencyGapReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompetencyGapReportRepository extends JpaRepository<CompetencyGapReport, UUID> {
    Optional<CompetencyGapReport> findByStudentIdAndRequisitionId(UUID studentId, UUID requisitionId);
}


