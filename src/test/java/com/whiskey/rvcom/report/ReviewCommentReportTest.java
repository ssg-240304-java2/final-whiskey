package com.whiskey.rvcom.report;

import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.report.service.ReviewCommentReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReviewCommentReportTest {

    private final ReviewCommentReportService reviewCommentReportService;

    @Autowired
    public ReviewCommentReportTest(ReviewCommentReportService reviewCommentReportService) {
        this.reviewCommentReportService = reviewCommentReportService;
    }

    @Test
    @DisplayName("리뷰댓글신고록목전체조회")
    public void findAll() {

        List<ReviewCommentReportDTO> reports  = reviewCommentReportService.getAllReviewCommentReports();

        for (ReviewCommentReportDTO report : reports) {
            System.out.println(report);
        }
    }

}
