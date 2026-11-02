package com.academia.placement.controller;

import com.academia.placement.dto.StudentProfileDto;
import com.academia.placement.dto.StudentSkillInput;
import com.academia.placement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@Validated
@Tag(name = "Student Profile & Skills", description = "Endpoints for managing student profiles and skill matrices")
@SecurityRequirement(name = "BearerAuth")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get profile of the currently logged-in student")
    public ResponseEntity<StudentProfileDto> getMyProfile(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(studentService.getProfileByUserId(userId));
    }

    @PutMapping("/me/skills")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Update verified skill matrix for current student")
    public ResponseEntity<StudentProfileDto> updateMySkills(@Valid @RequestBody List<StudentSkillInput> skills,
                                                           Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(studentService.updateSkillMatrix(userId, skills));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'RECRUITER', 'TPO_ADMIN')")
    @Operation(summary = "Get student profile by student ID")
    public ResponseEntity<StudentProfileDto> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getProfileById(id));
    }
}

