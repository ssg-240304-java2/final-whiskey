package com.whiskey.rvcom.entity.repository;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentReportRepository extends JpaRepository<ReviewCommentReport, Long> {
}
