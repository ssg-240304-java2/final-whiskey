package com.whiskey.rvcom.report;


import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import com.whiskey.rvcom.repository.RestaurantReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class RestaurantReportTest {


    private final RestaurantReportRepository restaurantReportRepository;

    private final RestaurantReportService restaurantReportService;

    @Autowired
    public RestaurantReportTest(RestaurantReportRepository restaurantReportRepository, RestaurantReportService restaurantReportService) {
        this.restaurantReportRepository = restaurantReportRepository;
        this.restaurantReportService = restaurantReportService;
    }


    @Test
    @DisplayName("식당신고목록전체조회")
    public void findAll() {

        List<RestaurantReportDTO> reports = restaurantReportService.getAllRestaurantReports();

        for (RestaurantReportDTO report : reports) {
            System.out.println(report);
        }
    }

}
