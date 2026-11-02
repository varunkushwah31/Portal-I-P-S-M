package com.academia.placement.repository;

import com.academia.placement.model.CompetencyGapReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompetencyGapReportRepository extends JpaRepository<CompetencyGapReport, UUID> {
    Optional<CompetencyGapReport> findByStudentIdAndRequisitionId(UUID studentId, UUID requisitionId);
}

