package com.whiskey.rvcom.report;

import com.whiskey.rvcom.entity.review.Rating;
import com.whiskey.rvcom.review.dto.ReviewCommentDTO;
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
        ReviewCommentDTO reviewCommentDTO = new ReviewCommentDTO(3L, false, LocalDateTime.now(), 3, 5, "여기 너무 좋아요! 호구당하기! 좋네요!", Rating.FIVE_STAR);
        ReviewCommentReportDTO reviewCommentReportDTO = new ReviewCommentReportDTO(null, false, true, LocalDateTime.now(), reviewCommentDTO, "댓글 신고 제목3", "댓글 신고 내용3");

        // when
        reviewCommentReportService.saveReviewCommentReport(reviewCommentReportDTO);

        // 조회 성공 후 목록 가져오기
        List<ReviewCommentReportDTO> list = reviewCommentReportService.getAllReviewCommentReports();


        // then 현재 테스트 코드로 등록한 신고와 DB에 등록된 신고의 내용이 같은지 비교
        Assertions.assertEquals(list.get(list.size()-1).getContent(), reviewCommentReportDTO.getContent());
    }
}
