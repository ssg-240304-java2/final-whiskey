package com.whiskey.rvcom.report;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.entity.report.ReviewReport;
import com.whiskey.rvcom.entity.review.Rating;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.entity.review.ReviewComment;
import com.whiskey.rvcom.review.dto.ReviewCommentDTO;
import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.report.service.ReviewCommentReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

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

        // given

        // when
        List<ReviewCommentReportDTO> reports  = reviewCommentReportService.getAllReviewCommentReports();

        for (ReviewCommentReportDTO report : reports) {
            System.out.println(report);
        }

        // then 조회한 신고목록이 null이 아닌지 확인
        Assertions.assertNotNull(reports);

    }


    @Test
    @DisplayName("댓글 신고 세부조회")
    public void findById() {

        // given
        Long id = 1L;

        // when
        ReviewCommentReportDTO result = reviewCommentReportService.getReviewCommentReport(id);

        System.out.println("result = " + result);

        // then Null 값 인지 확인 후 조회한 신고와 DB에 있는 신고의 ID가 같은지 확인
        Assertions.assertNotNull(result);

        System.out.println("result.getId() = " + result.getId());
        System.out.println("id = " + id);

        Assertions.assertEquals(id, result.getId());
    }


    @Test
    @DisplayName("댓글신고 결정 및 상태값 변경")
    public void commentReportPunish() {

        // given
        Long id = 1L;

        boolean isPunish = true;

        // when
        reviewCommentReportService.reviewCommentReportPunish(id, isPunish);

        // then 상태값 변경 후 확인
        ReviewCommentReportDTO result = reviewCommentReportService.getReviewCommentReport(id);
        System.out.println("isPunish = " + isPunish);
        System.out.println("result.isVisible() = " + result.isVisible());
        System.out.println("result.isChecked() = " + result.isChecked());

        Assertions.assertTrue(result.isChecked());
        Assertions.assertFalse(result.isVisible());
    }


    @Test
    @DisplayName("리뷰댓글신고등록")
    public void insertReviewCommentReport() {

        // given
        Long id = 3L;
        ReviewComment reviewComment = reviewCommentReportService.returnReviewComment(id);
        ReviewCommentReport report = new ReviewCommentReport();
        report.setTitle("신고등록 테스트코드");
        report.setContent("테스트");
        report.setReportedAt(LocalDateTime.now());
        report.setChecked(false);
        report.setVisible(true);
        report.setReviewComment(reviewComment);

        // when
        report.setReviewComment(reviewComment);
        reviewCommentReportService.saveReviewCommentReport(report);

        // 조회 성공 후 목록 가져오기
//        Page<ReviewCommentReportDTO> reports = reviewCommentReportService.getAllReviewCommentReports(10, "asc");


//        // then 현재 테스트 코드로 등록한 신고와 DB에 등록된 신고의 내용이 같은지 비교
//        Assertions.assertEquals(list.get(list.size()-1).getContent(), reviewCommentReport.getContent());
    }
}
