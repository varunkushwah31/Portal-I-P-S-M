package com.academia.placement.repository;

import com.academia.placement.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, UUID> {
    List<StudentSkill> findByStudentId(UUID studentId);
    Optional<StudentSkill> findByStudentIdAndSkillId(UUID studentId, UUID skillId);
    void deleteByStudentId(UUID studentId);
}

