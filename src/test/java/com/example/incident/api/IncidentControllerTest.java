package com.example.incident.api;

import com.example.incident.cassandra.IncidentRepository;
import com.example.incident.kafka.IncidentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IncidentControllerTest {
    private final IncidentRepository repository = mock(IncidentRepository.class);
    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, IncidentEvent> kafka = mock(KafkaTemplate.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new IncidentController(repository, kafka))
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void publishRejectsInvalidIncidentPayload() throws Exception {
        IncidentEvent invalidEvent = new IncidentEvent("", "payment-service", Instant.parse("2026-06-27T12:45:00Z"),
                "CRITICAL", "", null, "pay-01", "us-east-1", "OPEN", 300);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.fields.incidentId").exists())
                .andExpect(jsonPath("$.fields.message").exists());
    }

    @Test
    void publishSendsValidIncidentToKafka() throws Exception {
        IncidentEvent event = new IncidentEvent("inc-1", "payment-service", Instant.parse("2026-06-27T12:45:00Z"),
                "CRITICAL", "Database connection pool exhausted", null, "pay-01", "us-east-1", "OPEN", 300);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());

        verify(kafka).send("raw-incidents", "payment-service", event);
    }

    @Test
    void findByServiceRejectsUnsafeLimitValues() throws Exception {
        mockMvc.perform(get("/incidents")
                        .param("serviceName", "payment-service")
                        .param("day", "2026-06-27")
                        .param("limit", "1000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("limit must be between 1 and 500"));
    }
}
