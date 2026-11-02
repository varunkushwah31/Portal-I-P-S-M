package com.academia.placement.repository;

import com.academia.placement.model.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRequisitionRepository extends JpaRepository<JobRequisition, UUID> {
    List<JobRequisition> findByStatus(String status);
    List<JobRequisition> findByRecruiterId(UUID recruiterId);
}


