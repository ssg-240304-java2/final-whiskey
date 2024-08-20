package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.report.ReviewReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    Page<ReviewReport> findAllByIsCheckedFalse(Pageable pageable);
}
