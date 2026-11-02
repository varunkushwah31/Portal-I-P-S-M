package com.academia.placement.repository;

import com.academia.placement.model.CourseCurriculumMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseCurriculumMappingRepository extends JpaRepository<CourseCurriculumMapping, UUID> {
    List<CourseCurriculumMapping> findBySkillId(UUID skillId);
    Optional<CourseCurriculumMapping> findFirstBySkillId(UUID skillId);
}


