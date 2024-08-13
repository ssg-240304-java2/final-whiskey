package com.whiskey.rvcom.report;

import com.whiskey.rvcom.entity.review.Rating;
import com.whiskey.rvcom.review.dto.ReviewDTO;
import com.whiskey.rvcom.report.model.dto.ReviewReportDTO;
import com.whiskey.rvcom.report.service.ReviewReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ReviewReportTest {

    private final ReviewReportService reviewReportService;

    @Autowired
    public ReviewReportTest(ReviewReportService reviewReportService) {
        this.reviewReportService = reviewReportService;
    }


    @Test
    @DisplayName("리뷰신고록목전체조회")
    public void findAll() {

        // given

        // when
        List<ReviewReportDTO> reports = reviewReportService.getAllReviewReports();

        for (ReviewReportDTO report : reports) {
            System.out.println(report);
        }

        // then 조회한 신고목록이 null이 아닌지 확인
        Assertions.assertNotNull(reports);
    }


    @Test
    @DisplayName("리뷰 신고 세부조회")
    public void findById() {

        // given
        Long id = 2L;

        // when
        ReviewReportDTO result = reviewReportService.getReviewReport(id);

        System.out.println("id = " + id);
        System.out.println("result.getId() = " + result.getId());
        System.out.println("result = " + result);

        // then 조회한 신고의 id와 입력한 id가 같은지 확인
        Assertions.assertEquals(result.getId(), id);
    }


    @Test
    @DisplayName("리뷰신고등록")
    public void save() {

        // given
        ReviewDTO reviewDTO = new ReviewDTO(4L, false, LocalDateTime.now(), 4, 2, "가지마세요...", Rating.ONE_STAR);
        ReviewReportDTO reviewReportDTO =
                new ReviewReportDTO(null, false, true, LocalDateTime.now(), reviewDTO, "test2", "test2");

        // when
        reviewReportService.saveReviewReport(reviewReportDTO);

        // 조회 성공 후 목록 가져오기
        List<ReviewReportDTO> reports = reviewReportService.getAllReviewReports();

        // then 현재 테스트 코드로 등록한 신고와 DB에 등록된 신고의 내용이 같은지 비교
        Assertions.assertEquals(reports.get(reports.size()-1).getContent(), reviewReportDTO.getContent());
    }
}
