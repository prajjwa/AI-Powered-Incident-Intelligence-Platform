CREATE KEYSPACE IF NOT EXISTS incident_intelligence
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS incident_intelligence.incidents (
  service_name text,
  day date,
  event_time timestamp,
  incident_id text,
  severity text,
  message text,
  stacktrace text,
  hostname text,
  region text,
  status text,
  duration_seconds bigint,
  severity_score int,
  PRIMARY KEY ((service_name, day), event_time, incident_id)
) WITH CLUSTERING ORDER BY (event_time DESC);
