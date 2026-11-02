package com.academia.placement.dto;

import java.time.Instant;
import java.util.List;

public record ApiError(
        int status,
        String error,
        String message,
        Instant timestamp,
        List<String> details
) {
    public static ApiError of(int status, String error, String message) {
        return new ApiError(status, error, message, Instant.now(), List.of());
    }

    public static ApiError validation(int status, String message, List<String> details) {
        return new ApiError(status, "Validation Failed", message, Instant.now(), details);
    }
}
