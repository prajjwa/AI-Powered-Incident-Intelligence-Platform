package com.example.incident.mcp;

import com.example.incident.cassandra.*;
import com.example.incident.postgres.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mcp/tools")
public class McpController {
    private final IncidentRepository incidents;
    private final DailyReportRepository reports;

    public McpController(IncidentRepository incidents, DailyReportRepository reports) {
        this.incidents = incidents;
        this.reports = reports;
    }

    @GetMapping("findByService")
    public List<Incident> findByService(@RequestParam("serviceName") String serviceName, @RequestParam("day") LocalDate day) {
        return incidents.findRecentByServiceAndDay(serviceName, day, 100);
    }

    @GetMapping("generateDailyReport")
    public List<DailyReport> generateDailyReport(@RequestParam("day") LocalDate day) { return reports.findByReportDate(day); }

    @GetMapping("topRecurringIncidents")
    public List<DailyReport> topRecurringIncidents() { return reports.findTop5ByOrderByCriticalCountDesc(); }

    @GetMapping("searchByKeyword")
    public List<Incident> searchByKeyword(@RequestParam("serviceName") String serviceName, @RequestParam("day") LocalDate day, @RequestParam("keyword") String keyword) {
        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        if (normalizedKeyword.isEmpty()) {
            return List.of();
        }
        return incidents.findByServiceAndDay(serviceName, day).stream()
                .filter(i -> i.getMessage() != null && i.getMessage().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .limit(50)
                .toList();
    }
}
