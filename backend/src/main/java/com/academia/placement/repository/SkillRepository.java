package com.academia.placement.repository;

import com.academia.placement.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Optional<Skill> findByCanonicalName(String canonicalName);
}


