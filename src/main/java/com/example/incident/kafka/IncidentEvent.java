package com.example.incident.kafka;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record IncidentEvent(
        @NotBlank String incidentId,
        @NotBlank String serviceName,
        @NotNull Instant eventTime,
        @NotBlank String severity,
        @NotBlank String message,
        String stacktrace,
        @NotBlank String hostname,
        @NotBlank String region,
        @NotBlank String status,
        @Min(0) long durationSeconds) {}
