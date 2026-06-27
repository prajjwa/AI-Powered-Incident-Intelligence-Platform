package com.example.incident.api;

import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp, int status, String error, String message, Map<String, String> fields) {
    public static ApiError of(int status, String error, String message, Map<String, String> fields) {
        return new ApiError(Instant.now(), status, error, message, fields == null ? Map.of() : Map.copyOf(fields));
    }
}
