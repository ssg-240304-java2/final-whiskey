package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.entity.report.ReviewReport;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.service.ReviewReportService;
import com.whiskey.rvcom.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviewreport")
@RequiredArgsConstructor
public class ReviewReportController {

    private final ReviewReportService reviewReportService;
    private final ReviewService reviewService;

    /***
     * 리뷰 신고 등록
     * @param report
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<Void> registReviewReport(@RequestBody ReportData report) {

            ReviewReport reviewReport = new ReviewReport();
            reviewReport.setTitle(report.getTitle());
            reviewReport.setContent(report.getContent());
            reviewReport.setReportedAt(LocalDateTime.now());
            reviewReport.setChecked(false);
            reviewReport.setVisible(true);

            reviewReport.setReview(reviewReportService.returnReview(report.getId()));

            reviewReportService.saveReviewReport(reviewReport);
            return ResponseEntity.ok().build();
    }

    /***
     * 리뷰신고 전체 조회
     * @param page
     * @param sortOrder
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Page<ReviewReport> getReviewReports(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "asc") String sortOrder) {
        return reviewReportService.getAllReviewReports(page, sortOrder);
    }

    /***
     * 리뷰신고 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/detail/{reportId}")
    public ResponseEntity<Map<String, Object>> getReportDetail(@PathVariable("reportId") Long id) {

        Map<String, Object> response = new HashMap<>();

        ReviewReport report = reviewReportService.getReviewReport(id);

        if (report != null) {
            response.put("report", report);
        } else {
            response.put("report", "report not found");
        }
        return ResponseEntity.ok(response);
    }

    /***
     * 리뷰신고 처리 (신고와 리뷰 상태값 변경)
     * @param reportId
     * @param btnId
     * @return
     */
    @PutMapping("/update/{reportId}")
    public ResponseEntity<Void> updateReport(@PathVariable Long reportId, @RequestParam String btnId) {

        boolean isPunish = btnId.equals("punish");

        reviewReportService.reviewReportPunish(reportId, isPunish);
        ReviewReport reviewReport = reviewReportService.getReviewReport(reportId);

        if(isPunish) {
            Review review = reviewReport.getReview();
            // 리뷰 상태값 변경 (노출 여부)
            review.setSuspended(isPunish);

            reviewService.saveReview(review);

            // 메일 발송 코드 예정
        }
        return ResponseEntity.ok().build(); // 명시적으로 상태 코드 200 OK를 반환
    }

}
