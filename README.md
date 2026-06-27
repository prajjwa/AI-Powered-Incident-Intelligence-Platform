# AI-Powered Incident Intelligence Platform

A production-style Java 21 / Spring Boot 3 backend that ingests infrastructure incidents through Kafka, stores immutable time-series events in Cassandra, runs Spring Batch aggregations into PostgreSQL, exposes operational APIs, and provides MCP-style tools for AI assistants.

## Architecture

```text
Log Generator -> Kafka raw-incidents -> Incident Consumer -> Cassandra incidents
                                      -> Batch Aggregation -> PostgreSQL reports
                                      -> MCP Tools -> Claude / Cursor / ChatGPT
```

## Implemented capabilities

- Kafka topic contract for `raw-incidents` and JSON incident publishing.
- Cassandra write-heavy incident table partitioned by `(service_name, day)` and clustered by descending event time.
- Spring Batch `dailyAggregationJob` that calculates counts, critical totals, average duration, and a generated summary.
- PostgreSQL `daily_reports` reporting model.
- MCP-style REST tools for service lookup, daily reports, recurring incident reports, and keyword search.
- Request validation with structured JSON errors for invalid incident payloads and unsafe query limits.
- Docker Compose services for Kafka, Kafka UI, Cassandra, PostgreSQL, Redis, Prometheus, and Grafana.

## Key endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/incidents` | Publish an incident to Kafka `raw-incidents`. |
| `GET` | `/incidents?serviceName=payment-service&day=2026-06-27&limit=100` | Read recent Cassandra incidents with a bounded `limit` from 1 to 500. |
| `GET` | `/reports/daily?date=2026-06-27` | Read daily PostgreSQL reports. |
| `POST` | `/reports/batch/run?date=2026-06-27&serviceName=payment-service` | Run daily aggregation. |
| `GET` | `/mcp/tools/findByService` | MCP-compatible service incident lookup. |
| `GET` | `/mcp/tools/topRecurringIncidents` | MCP-compatible recurring failure report. |

## Run locally

```bash
docker compose up -d kafka zookeeper cassandra postgres redis prometheus grafana
mvn spring-boot:run
```

Initialize Cassandra manually if needed:

```bash
docker compose exec -T cassandra cqlsh < db/cassandra-schema.cql
```

## Example incident

```bash
curl -X POST localhost:8080/incidents \
  -H 'content-type: application/json' \
  -d '{"incidentId":"inc-1","serviceName":"payment-service","eventTime":"2026-06-27T12:45:00Z","severity":"CRITICAL","message":"Database connection pool exhausted","hostname":"pay-01","region":"us-east-1","status":"OPEN","durationSeconds":300}'
```


## Validation behavior

Incident ingestion requires non-empty incident identifiers, service names, severities, messages, hostnames, regions, statuses, and event timestamps. Invalid requests return a structured `400 Bad Request` response with field-level details so API clients and AI tools can repair inputs deterministically.
