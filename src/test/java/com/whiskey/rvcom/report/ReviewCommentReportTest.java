package com.whiskey.rvcom.report;

import com.whiskey.rvcom.entity.review.Rating;
import com.whiskey.rvcom.report.model.dto.ReviewCommentDTO;
import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.report.service.ReviewCommentReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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

        // 조회한 신고목록이 null이 아닌지 확인
        Assertions.assertNotNull(reports);

    }
}
