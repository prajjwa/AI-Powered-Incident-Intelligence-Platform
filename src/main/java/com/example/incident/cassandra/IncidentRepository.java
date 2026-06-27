package com.example.incident.cassandra;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface IncidentRepository extends CassandraRepository<Incident, IncidentKey> {
    @Query("SELECT * FROM incidents WHERE service_name = ?0 AND day = ?1 LIMIT ?2")
    List<Incident> findRecentByServiceAndDay(String serviceName, LocalDate day, int limit);

    @Query("SELECT * FROM incidents WHERE service_name = ?0 AND day = ?1")
    List<Incident> findByServiceAndDay(String serviceName, LocalDate day);
}
