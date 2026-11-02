package com.academia.placement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.academia.placement.model.StudentProfile;

    public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    Optional<StudentProfile> findByUserId(UUID userId);
    Optional<StudentProfile> findByRollNumber(String rollNumber);
    boolean existsByRollNumber(String rollNumber);

    long countByGraduationYear(Integer graduationYear);
    long countByGraduationYearAndIsPlacedTrue(Integer graduationYear);

    @Query("SELECT AVG(s.cgpa) FROM StudentProfile s WHERE s.graduationYear = :year")
    Double findAverageCgpaByGraduationYear(Integer year);
}


