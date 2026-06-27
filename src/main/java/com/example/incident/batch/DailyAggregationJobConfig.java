package com.example.incident.batch;

import com.example.incident.cassandra.Incident;
import com.example.incident.cassandra.IncidentRepository;
import com.example.incident.postgres.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DailyAggregationJobConfig {
    @Bean
    Job dailyAggregationJob(JobRepository jobRepository, Step aggregateDailyIncidents) {
        return new JobBuilder("dailyAggregationJob", jobRepository).start(aggregateDailyIncidents).build();
    }

    @Bean
    Step aggregateDailyIncidents(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                IncidentRepository incidents, DailyReportRepository reports) {
        return new StepBuilder("aggregateDailyIncidents", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JobParameters params = contribution.getStepExecution().getJobParameters();
                    LocalDate reportDate = params.getLocalDate("reportDate");
                    String serviceName = params.getString("serviceName");
                    List<Incident> rows = incidents.findByServiceAndDay(serviceName, reportDate);
                    DailyReport report = new DailyReport();
                    report.setReportDate(reportDate);
                    report.setServiceName(serviceName);
                    report.setIncidentCount(rows.size());
                    report.setCriticalCount(rows.stream().filter(i -> "CRITICAL".equalsIgnoreCase(i.getSeverity())).count());
                    report.setAverageDurationSeconds(rows.stream().mapToLong(Incident::getDurationSeconds).average().orElse(0));
                    report.setAiSummary(summary(serviceName, report.getIncidentCount(), report.getCriticalCount()));
                    reports.save(report);
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    private String summary(String serviceName, long total, long critical) {
        return "%s experienced %d incidents, including %d critical incidents. Investigate recurring error signatures and capacity saturation."
                .formatted(serviceName, total, critical);
    }
}
