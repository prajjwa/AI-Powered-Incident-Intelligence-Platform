package com.example.incident;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.example.incident.cassandra")
@EnableJpaRepositories(basePackages = "com.example.incident.postgres")
public class IncidentIntelligenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IncidentIntelligenceApplication.class, args);
    }
}
