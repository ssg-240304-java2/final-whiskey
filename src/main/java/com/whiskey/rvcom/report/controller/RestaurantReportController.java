package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/restaurantreport")
@RequiredArgsConstructor
public class RestaurantReportController {

    private final RestaurantReportService restaurantReportService;

    @PostMapping("/regist")
    public void registRestaurantReport(@RequestBody ReportData report) {

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
    public ResponseEntity<List<RestaurantReportDTO>> getReports() {
        List<RestaurantReportDTO> reports = restaurantReportService.getAllRestaurantReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/detail")
    public String getReportDetail(@RequestParam Long id) {
        RestaurantReportDTO report = restaurantReportService.getRestaurantReport(id);

        return "report/restaurantDetail";
    }


}
