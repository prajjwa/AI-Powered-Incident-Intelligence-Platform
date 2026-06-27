package com.example.incident.api;

import com.example.incident.postgres.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final DailyReportRepository reports;
    private final JobLauncher jobLauncher;
    private final Job dailyAggregationJob;

    public ReportController(DailyReportRepository reports, JobLauncher jobLauncher, Job dailyAggregationJob) {
        this.reports = reports;
        this.jobLauncher = jobLauncher;
        this.dailyAggregationJob = dailyAggregationJob;
    }

    @GetMapping("/daily")
    public List<DailyReport> daily(@RequestParam LocalDate date) { return reports.findByReportDate(date); }

    @PostMapping("/batch/run")
    public String run(@RequestParam LocalDate date, @RequestParam(defaultValue = "payment-service") String serviceName) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLocalDate("reportDate", date)
                .addString("serviceName", serviceName)
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();
        return jobLauncher.run(dailyAggregationJob, params).getStatus().toString();
    }
}
