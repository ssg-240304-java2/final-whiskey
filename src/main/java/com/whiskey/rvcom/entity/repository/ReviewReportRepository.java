package com.whiskey.rvcom.entity.repository;

import com.whiskey.rvcom.entity.report.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
}