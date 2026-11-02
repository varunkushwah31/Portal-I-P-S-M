package com.academia.placement.repository;

import com.academia.placement.model.CourseCurriculumMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseCurriculumMappingRepository extends JpaRepository<CourseCurriculumMapping, UUID> {
    List<CourseCurriculumMapping> findBySkillId(UUID skillId);
    Optional<CourseCurriculumMapping> findFirstBySkillId(UUID skillId);
}

