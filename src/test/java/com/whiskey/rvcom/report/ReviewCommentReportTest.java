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


    @Test
    @DisplayName("리뷰댓글신고등록")
    public void insertReviewCommentReport() {

        ReviewCommentDTO reviewCommentDTO = new ReviewCommentDTO(3L, false, LocalDateTime.now(), 3, 5, "여기 너무 좋아요! 호구당하기! 좋네요!", Rating.FIVE_STAR);
        ReviewCommentReportDTO reviewCommentReportDTO = new ReviewCommentReportDTO(null, false, true, LocalDateTime.now(), reviewCommentDTO, "댓글 신고 제목3", "댓글 신고 내용3");

        reviewCommentReportService.saveReviewCommentReport(reviewCommentReportDTO);

        // 조회 성공 후 목록 가져오기
        List<ReviewCommentReportDTO> list = reviewCommentReportService.getAllReviewCommentReports();

//      현재 테스트 코드로 등록한 신고와 DB에 등록된 신고의 내용이 같은지 비교
        Assertions.assertEquals(list.get(list.size()-1).getContent(), reviewCommentReportDTO.getContent());
    }

}
