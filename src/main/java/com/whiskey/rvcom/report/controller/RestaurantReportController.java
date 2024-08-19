package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @GetMapping("/detail/{reportId}")
    public ResponseEntity<Map<String, Object>> getReportDetail(@PathVariable("reportId") Long id) {

        Map<String, Object> response = new HashMap<>();

        RestaurantReportDTO report = restaurantReportService.getRestaurantReport(id);

        if (report != null) {
            response.put("report", report);
        } else {
            response.put("report", "report not found");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{reportId}")
    public void updateReport(@PathVariable Long reportId, @RequestParam String btnId) {
        System.out.println("btnId = " + btnId);
        System.out.println("reportId = " + reportId);
        boolean isPunish = btnId.equals("punish");

        restaurantReportService.restaurantReportPunish(reportId, isPunish);
    }
}
