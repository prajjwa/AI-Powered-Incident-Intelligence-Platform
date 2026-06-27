package com.example.incident.mcp;

import com.example.incident.cassandra.*;
import com.example.incident.postgres.*;
import java.time.LocalDate;
import java.util.List;
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
    public List<Incident> findByService(@RequestParam String serviceName, @RequestParam LocalDate day) {
        return incidents.findRecentByServiceAndDay(serviceName, day, 100);
    }

    @GetMapping("generateDailyReport")
    public List<DailyReport> generateDailyReport(@RequestParam LocalDate day) { return reports.findByReportDate(day); }

    @GetMapping("topRecurringIncidents")
    public List<DailyReport> topRecurringIncidents() { return reports.findTop5ByOrderByCriticalCountDesc(); }

    @GetMapping("searchByKeyword")
    public List<Incident> searchByKeyword(@RequestParam String serviceName, @RequestParam LocalDate day, @RequestParam String keyword) {
        return incidents.findByServiceAndDay(serviceName, day).stream()
                .filter(i -> i.getMessage() != null && i.getMessage().toLowerCase().contains(keyword.toLowerCase()))
                .limit(50)
                .toList();
    }
}
