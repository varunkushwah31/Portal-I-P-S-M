package com.academia.placement.repository;

import com.academia.placement.model.RequisitionSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequisitionSkillRepository extends JpaRepository<RequisitionSkill, UUID> {
    List<RequisitionSkill> findByRequisitionId(UUID requisitionId);
}

