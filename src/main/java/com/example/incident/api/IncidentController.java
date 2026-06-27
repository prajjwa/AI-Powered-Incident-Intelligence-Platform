package com.example.incident.api;

import com.example.incident.cassandra.Incident;
import com.example.incident.cassandra.IncidentRepository;
import com.example.incident.kafka.IncidentEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/incidents")
public class IncidentController {
    private static final String RAW_INCIDENTS_TOPIC = "raw-incidents";

    private final IncidentRepository incidents;
    private final KafkaTemplate<String, IncidentEvent> kafka;

    public IncidentController(IncidentRepository incidents, KafkaTemplate<String, IncidentEvent> kafka) {
        this.incidents = incidents;
        this.kafka = kafka;
    }

    @GetMapping
    public List<Incident> findByService(@RequestParam("serviceName") String serviceName, @RequestParam("day") LocalDate day,
                                        @RequestParam(name = "limit", defaultValue = "100") @Min(1) @Max(500) int limit) {
        if (limit < 1 || limit > 500) {
            throw new IllegalArgumentException("limit must be between 1 and 500");
        }
        return incidents.findRecentByServiceAndDay(serviceName, day, limit);
    }

    @PostMapping
    public void publish(@Valid @RequestBody IncidentEvent event) {
        kafka.send(RAW_INCIDENTS_TOPIC, event.serviceName(), event);
    }
}
