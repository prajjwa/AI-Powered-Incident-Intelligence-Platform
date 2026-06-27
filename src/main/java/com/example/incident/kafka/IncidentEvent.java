package com.example.incident.kafka;

import java.time.Instant;

public record IncidentEvent(String incidentId, String serviceName, Instant eventTime, String severity,
                            String message, String stacktrace, String hostname, String region,
                            String status, long durationSeconds) {}
