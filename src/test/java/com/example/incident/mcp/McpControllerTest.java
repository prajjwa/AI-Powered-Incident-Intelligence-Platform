package com.example.incident.mcp;

import com.example.incident.cassandra.IncidentRepository;
import com.example.incident.postgres.DailyReportRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class McpControllerTest {
    @Test
    void searchByKeywordReturnsNoResultsForBlankKeywordWithoutQueryingCassandra() {
        IncidentRepository incidents = mock(IncidentRepository.class);
        DailyReportRepository reports = mock(DailyReportRepository.class);
        McpController controller = new McpController(incidents, reports);

        assertThat(controller.searchByKeyword("payment-service", LocalDate.parse("2026-06-27"), "   ")).isEmpty();
        verifyNoInteractions(incidents);
    }
}
