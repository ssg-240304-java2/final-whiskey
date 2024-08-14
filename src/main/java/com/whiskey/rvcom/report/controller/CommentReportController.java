package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.report.service.ReviewCommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/commentreport")
@RequiredArgsConstructor
public class CommentReportController {

    private final ReviewCommentReportService reviewCommentReportService;

    @PostMapping("/regist")
    public void registCommentReport(@RequestBody ReportData report){

        System.out.println("report = " + report.getId());
        System.out.println("report = " + report.getContent());
        System.out.println("report = " + report.getTitle());

        ReviewCommentReportDTO reviewCommentReportDTO = new ReviewCommentReportDTO();
        reviewCommentReportDTO.setTitle(report.getTitle());
        reviewCommentReportDTO.setContent(report.getContent());
        reviewCommentReportDTO.setReportedAt(LocalDateTime.now());
        reviewCommentReportDTO.setChecked(false);
        reviewCommentReportDTO.setVisible(true);
        reviewCommentReportDTO.setId(null);

        reviewCommentReportDTO.setReviewCommentDTO(reviewCommentReportService.returnReviewCommentDTO(report.getId()));

        reviewCommentReportService.saveReviewCommentReport(reviewCommentReportDTO);
    }
}
