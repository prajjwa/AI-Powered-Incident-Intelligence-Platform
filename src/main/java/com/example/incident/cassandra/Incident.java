package com.example.incident.cassandra;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("incidents")
public class Incident {
    @PrimaryKey private IncidentKey key;
    private String severity;
    private String message;
    private String stacktrace;
    private String hostname;
    private String region;
    private String status;
    @Column("duration_seconds") private long durationSeconds;
    @Column("severity_score") private int severityScore;

    public IncidentKey getKey() { return key; }
    public void setKey(IncidentKey key) { this.key = key; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStacktrace() { return stacktrace; }
    public void setStacktrace(String stacktrace) { this.stacktrace = stacktrace; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }
    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }
}
