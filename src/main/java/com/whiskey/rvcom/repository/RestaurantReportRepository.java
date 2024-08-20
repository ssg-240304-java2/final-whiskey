package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.report.RestaurantReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RestaurantReportRepository extends JpaRepository<RestaurantReport, Long> {

    Page<RestaurantReport> findAllByIsCheckedFalse(Pageable pageable);


}
