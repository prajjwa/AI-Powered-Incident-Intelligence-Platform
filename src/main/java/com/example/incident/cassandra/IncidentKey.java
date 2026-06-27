package com.example.incident.cassandra;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public record IncidentKey(
        @PrimaryKeyColumn(name = "service_name", type = PrimaryKeyType.PARTITIONED) String serviceName,
        @PrimaryKeyColumn(name = "day", type = PrimaryKeyType.PARTITIONED) LocalDate day,
        @PrimaryKeyColumn(name = "event_time", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING) Instant eventTime,
        @PrimaryKeyColumn(name = "incident_id", type = PrimaryKeyType.CLUSTERED) String incidentId) implements Serializable {}
