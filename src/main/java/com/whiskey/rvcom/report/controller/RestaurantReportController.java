package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/restaurantreport")
@RequiredArgsConstructor
public class RestaurantReportController {

    private final RestaurantReportService restaurantReportService;

    @PostMapping("/regist")
    public void registRestaurantReport(@RequestBody ReportData report) {

        System.out.println("report = " + report.getId());
        System.out.println("report = " + report.getContent());
        System.out.println("report = " + report.getTitle());

        RestaurantReportDTO restaurantReportDTO = new RestaurantReportDTO();
        restaurantReportDTO.setTitle(report.getTitle());
        restaurantReportDTO.setContent(report.getContent());
        restaurantReportDTO.setReportedAt(LocalDateTime.now());
        restaurantReportDTO.setChecked(false);
        restaurantReportDTO.setVisible(true);

        restaurantReportDTO.setRestaurantDTO(restaurantReportService.returnRestaurantDTO(report.getId()));

        restaurantReportService.saveRestaurantReport(restaurantReportDTO);
    }

    @GetMapping("/list")
    public List<RestaurantReportDTO> getReports() {
        return restaurantReportService.getAllRestaurantReports();
    }


}
