package com.whiskey.rvcom.report.controller;


import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.report.model.dto.RestaurantDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.ReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/restaurant")
    public void createRestaurantReport() {

        // 임시로 생성
        RestaurantReportDTO TempRestaurantReport = new RestaurantReportDTO(1L, "영업시간이 이상해요!!", "아약스 커리의 영업시간! 어쩌고 저쩌고", LocalDateTime.now(), false, true,
                new RestaurantDTO(1L, "아약스커리", RestaurantCategory.KOREAN, "000-000-0000", true));

        reportService.createRestaurantReport(TempRestaurantReport);

    }
}
