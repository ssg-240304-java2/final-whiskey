package com.whiskey.rvcom;


import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.ReportService;
import com.whiskey.rvcom.repository.RestaurantReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class ReportTest {

    @Autowired
    private RestaurantReportRepository restaurantReportRepository;

    @Autowired
    private ReportService reportService;


    @Test
    @DisplayName("신고목록전체조회")
    public void findAll() {

        List<RestaurantReportDTO> reports = reportService.getAllRestaurantReports();

        System.out.println("reports : " + reports);

    }
}
