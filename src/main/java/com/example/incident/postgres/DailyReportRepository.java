package com.example.incident.postgres;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    List<DailyReport> findByReportDate(LocalDate reportDate);
    List<DailyReport> findTop5ByOrderByCriticalCountDesc();
}
