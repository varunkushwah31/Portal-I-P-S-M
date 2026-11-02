package com.academia.placement.controller;

import com.academia.placement.dto.SkillDto;
import com.academia.placement.repository.SkillRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
@Tag(name = "Skill Taxonomy", description = "Endpoints for normalized canonical skills")
public class SkillController {

    private final SkillRepository skillRepository;

    public SkillController(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @GetMapping
    @Operation(summary = "Get all normalized skills in the institutional taxonomy")
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        List<SkillDto> skills = skillRepository.findAll().stream()
                .map(s -> new SkillDto(
                        s.getId(),
                        s.getCanonicalName(),
                        s.getCategory() != null ? s.getCategory().getName() : "General",
                        s.getDescription()
                ))
                .toList();

        return ResponseEntity.ok(skills);
    }
}
