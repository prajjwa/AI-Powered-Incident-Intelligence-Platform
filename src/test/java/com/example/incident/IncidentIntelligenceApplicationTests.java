package com.example.incident;

import org.junit.jupiter.api.Test;

class IncidentIntelligenceApplicationTests {
    @Test
    void projectHasApplicationEntrypoint() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> Class.forName("com.example.incident.IncidentIntelligenceApplication"));
    }
}
