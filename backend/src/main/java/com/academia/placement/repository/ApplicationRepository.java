package com.academia.placement.repository;

import com.academia.placement.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findByStudentId(UUID studentId);
    List<Application> findByRequisitionId(UUID requisitionId);
    Optional<Application> findByRequisitionIdAndStudentId(UUID requisitionId, UUID studentId);
    boolean existsByRequisitionIdAndStudentId(UUID requisitionId, UUID studentId);
}


