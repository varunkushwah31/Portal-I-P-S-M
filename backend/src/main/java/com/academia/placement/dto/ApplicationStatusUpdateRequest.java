package com.academia.placement.dto;

import com.academia.placement.model.ApplicationStage;
import jakarta.validation.constraints.NotNull;

public class ApplicationStatusUpdateRequest {

    @NotNull
    private ApplicationStage stage;

    public ApplicationStatusUpdateRequest() {}

    public ApplicationStatusUpdateRequest(ApplicationStage stage) {
        this.stage = stage;
    }

    public ApplicationStage getStage() { return stage; }
    public void setStage(ApplicationStage stage) { this.stage = stage; }
}

