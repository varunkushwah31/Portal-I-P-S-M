package com.academia.placement.dto;

import java.util.UUID;

public class SkillDto {
    private UUID id;
    private String canonicalName;
    private String category;
    private String description;

    public SkillDto() {}

    public SkillDto(UUID id, String canonicalName, String category, String description) {
        this.id = id;
        this.canonicalName = canonicalName;
        this.category = category;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCanonicalName() { return canonicalName; }
    public void setCanonicalName(String canonicalName) { this.canonicalName = canonicalName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

