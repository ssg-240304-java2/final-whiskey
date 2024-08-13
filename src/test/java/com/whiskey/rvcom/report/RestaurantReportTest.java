package com.whiskey.rvcom.report;


import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.report.model.dto.RestaurantDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class RestaurantReportTest {



    private final RestaurantReportService restaurantReportService;

    @Autowired
    public RestaurantReportTest(RestaurantReportService restaurantReportService) {
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


    @Test
    @DisplayName("식당신고등록")
    public void save() {
        RestaurantDTO restaurantDTO = new RestaurantDTO(2L, "아약스커리", RestaurantCategory.KOREAN, "000-000-0000", true);
        RestaurantReportDTO report = new RestaurantReportDTO(null, "신고 제목3", "신고 내용3", LocalDateTime.now(), false, true, restaurantDTO);

        restaurantReportService.saveRestaurantReport(report);
    }

}
