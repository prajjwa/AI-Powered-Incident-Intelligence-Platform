package com.example.incident.postgres;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_reports")
public class DailyReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private LocalDate reportDate;
    private String serviceName;
    private long incidentCount;
    private long criticalCount;
    private double averageDurationSeconds;
    @Column(length = 4096) private String aiSummary;

    public Long getId() { return id; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public long getIncidentCount() { return incidentCount; }
    public void setIncidentCount(long incidentCount) { this.incidentCount = incidentCount; }
    public long getCriticalCount() { return criticalCount; }
    public void setCriticalCount(long criticalCount) { this.criticalCount = criticalCount; }
    public double getAverageDurationSeconds() { return averageDurationSeconds; }
    public void setAverageDurationSeconds(double averageDurationSeconds) { this.averageDurationSeconds = averageDurationSeconds; }
    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
}
