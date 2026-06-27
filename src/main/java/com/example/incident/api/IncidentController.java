package com.example.incident.api;

import com.example.incident.cassandra.*;
import com.example.incident.kafka.IncidentEvent;
import java.time.LocalDate;
import java.util.List;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incidents")
public class IncidentController {
    private final IncidentRepository incidents;
    private final KafkaTemplate<String, IncidentEvent> kafka;

    public IncidentController(IncidentRepository incidents, KafkaTemplate<String, IncidentEvent> kafka) {
        this.incidents = incidents;
        this.kafka = kafka;
    }

    @GetMapping
    public List<Incident> findByService(@RequestParam String serviceName, @RequestParam LocalDate day,
                                        @RequestParam(defaultValue = "100") int limit) {
        return incidents.findRecentByServiceAndDay(serviceName, day, limit);
    }

    @PostMapping
    public void publish(@RequestBody IncidentEvent event) {
        kafka.send("raw-incidents", event.serviceName(), event);
    }
}
