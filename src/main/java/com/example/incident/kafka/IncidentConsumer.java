package com.example.incident.kafka;

import com.example.incident.cassandra.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentConsumer {
    private final IncidentRepository incidents;

    public IncidentConsumer(IncidentRepository incidents) { this.incidents = incidents; }

    @KafkaListener(topics = "raw-incidents", groupId = "incident-writer")
    public void consume(IncidentEvent event) {
        Incident incident = new Incident();
        LocalDate day = LocalDate.ofInstant(event.eventTime(), ZoneOffset.UTC);
        incident.setKey(new IncidentKey(event.serviceName(), day, event.eventTime(), event.incidentId()));
        incident.setSeverity(event.severity());
        incident.setMessage(event.message());
        incident.setStacktrace(event.stacktrace());
        incident.setHostname(event.hostname());
        incident.setRegion(event.region());
        incident.setStatus(event.status());
        incident.setDurationSeconds(event.durationSeconds());
        incident.setSeverityScore(score(event.severity()));
        incidents.save(incident);
    }

    private int score(String severity) {
        return switch (severity == null ? "" : severity.toUpperCase()) {
            case "CRITICAL" -> 100;
            case "HIGH" -> 75;
            case "MEDIUM" -> 50;
            case "LOW" -> 25;
            default -> 10;
        };
    }
}
