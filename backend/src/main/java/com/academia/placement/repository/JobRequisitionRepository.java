package com.academia.placement.repository;

import com.academia.placement.model.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRequisitionRepository extends JpaRepository<JobRequisition, UUID> {
    List<JobRequisition> findByStatus(String status);
    List<JobRequisition> findByRecruiterId(UUID recruiterId);
}

