package com.academia.placement.service;

import com.academia.placement.dto.StudentProfileDto;
import com.academia.placement.dto.StudentSkillDto;
import com.academia.placement.dto.StudentSkillInput;
import com.academia.placement.model.*;
import com.academia.placement.repository.SkillRepository;
import com.academia.placement.repository.StudentProfileRepository;
import com.academia.placement.repository.StudentSkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentProfileRepository studentProfileRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final SkillRepository skillRepository;
    private final AuditService auditService;

    public StudentService(StudentProfileRepository studentProfileRepository,
                          StudentSkillRepository studentSkillRepository,
                          SkillRepository skillRepository,
                          AuditService auditService) {
        this.studentProfileRepository = studentProfileRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.skillRepository = skillRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public StudentProfileDto getProfileByUserId(UUID userId) {
        StudentProfile student = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user id: " + userId));

        return toDto(student);
    }

    @Transactional(readOnly = true)
    public StudentProfileDto getProfileById(UUID studentId) {
        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for id: " + studentId));

        return toDto(student);
    }

    @Transactional
    public StudentProfileDto updateSkillMatrix(UUID userId, List<StudentSkillInput> skillInputs) {
        StudentProfile student = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for user id: " + userId));

        studentSkillRepository.deleteByStudentId(student.getId());

        for (StudentSkillInput input : skillInputs) {
            Skill skill = skillRepository.findById(input.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found for id: " + input.getSkillId()));

            StudentSkill studentSkill = new StudentSkill(
                    student,
                    skill,
                    input.getProficiencyLevel(),
                    true,
                    "SELF_REPORTED_STUDENT"
            );
            studentSkillRepository.save(studentSkill);
        }

        auditService.recordEvent(
                userId,
                "STUDENT",
                "UPDATE_SKILL_MATRIX",
                "StudentProfile",
                student.getId(),
                "Updated technical skill matrix count: " + skillInputs.size(),
                "0.0.0.0"
        );

        StudentProfile updatedStudent = studentProfileRepository.findById(student.getId()).orElseThrow();
        return toDto(updatedStudent);
    }

    public StudentProfileDto toDto(StudentProfile s) {
        List<StudentSkillDto> skillDtos = s.getSkills().stream()
                .map(sk -> new StudentSkillDto(
                        sk.getSkill().getId(),
                        sk.getSkill().getCanonicalName(),
                        sk.getSkill().getCategory() != null ? sk.getSkill().getCategory().getName() : "General",
                        sk.getProficiencyLevel(),
                        sk.isVerified(),
                        sk.getVerificationSource()
                ))
                .collect(Collectors.toList());

        return new StudentProfileDto(
                s.getId(),
                s.getUser() != null ? s.getUser().getId() : null,
                s.getRollNumber(),
                s.getFullName(),
                s.getDepartment(),
                s.getCgpa(),
                s.getGraduationYear(),
                s.isPlaced(),
                skillDtos
        );
    }
}

