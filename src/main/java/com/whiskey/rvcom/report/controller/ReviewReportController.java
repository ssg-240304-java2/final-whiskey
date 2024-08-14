package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.model.dto.ReviewReportDTO;
import com.whiskey.rvcom.report.service.ReviewReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reviewreport")
@RequiredArgsConstructor
public class ReviewReportController {

    private final ReviewReportService reviewReportService;

    @PostMapping("/regist")
    public void registReviewReport(@RequestBody ReportData report) {

            System.out.println("report = " + report.getId());
            System.out.println("report = " + report.getContent());
            System.out.println("report = " + report.getTitle());

            ReviewReportDTO reviewReportDTO = new ReviewReportDTO();

            reviewReportDTO.setTitle(report.getTitle());
            reviewReportDTO.setContent(report.getContent());
            reviewReportDTO.setReportedAt(LocalDateTime.now());
            reviewReportDTO.setChecked(false);
            reviewReportDTO.setVisible(true);
            reviewReportDTO.setId(null);

            reviewReportDTO.setReviewDTO(reviewReportService.returnReviewDTO(report.getId()));

            reviewReportService.saveReviewReport(reviewReportDTO);
    }
}
