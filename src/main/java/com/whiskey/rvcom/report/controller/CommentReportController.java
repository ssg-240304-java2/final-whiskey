package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.report.service.ReviewCommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reviewcommentreport")
@RequiredArgsConstructor
public class CommentReportController {

    private final ReviewCommentReportService reviewCommentReportService;

    @PostMapping("/regist")
    public ResponseEntity<Void> registCommentReport(@RequestBody ReportData report){

        ReviewCommentReport reviewCommentReport = new ReviewCommentReport();
        reviewCommentReport.setTitle(report.getTitle());
        reviewCommentReport.setContent(report.getContent());
        reviewCommentReport.setReportedAt(LocalDateTime.now());
        reviewCommentReport.setChecked(false);
        reviewCommentReport.setVisible(true);

        reviewCommentReport.setReviewComment(reviewCommentReportService.returnReviewComment(report.getId()));

        reviewCommentReportService.saveReviewCommentReport(reviewCommentReport);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<ReviewCommentReport> getReviewCommentReports(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "asc") String sortOrder) {
        return reviewCommentReportService.getAllReviewCommentReports(page, sortOrder);
    }
}
