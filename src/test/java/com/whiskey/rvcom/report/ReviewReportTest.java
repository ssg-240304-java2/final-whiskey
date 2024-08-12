package com.whiskey.rvcom.report;

import com.whiskey.rvcom.report.model.dto.ReviewReportDTO;
import com.whiskey.rvcom.report.service.ReviewReportService;
import com.whiskey.rvcom.repository.ReviewReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReviewReportTest {

    private final ReviewReportRepository reviewReportRepository;
    private final ReviewReportService reviewReportService;

    @Autowired
    public ReviewReportTest(ReviewReportRepository reviewReportRepository, ReviewReportService reviewReportService) {
        this.reviewReportRepository = reviewReportRepository;
        this.reviewReportService = reviewReportService;
    }


    @Test
    @DisplayName("리뷰신고록목전체조회")
    public void findAll() {

        List<ReviewReportDTO> reports = reviewReportService.getAllReviewReports();

        for (ReviewReportDTO report : reports) {
            System.out.println(report);
        }
    }
}
