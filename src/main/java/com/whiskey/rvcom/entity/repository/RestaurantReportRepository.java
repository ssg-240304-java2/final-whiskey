package com.whiskey.rvcom.entity.repository;

import com.whiskey.rvcom.entity.report.RestaurantReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReportRepository extends JpaRepository<RestaurantReport, Long> {
}
