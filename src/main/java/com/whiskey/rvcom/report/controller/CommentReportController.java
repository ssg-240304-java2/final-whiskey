package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.entity.review.ReviewComment;
import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.service.ReviewCommentReportService;
import com.whiskey.rvcom.review.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviewcommentreport")
@RequiredArgsConstructor
public class CommentReportController {

    private final ReviewCommentReportService reviewCommentReportService;
    private final ReviewCommentService reviewCommentService;

    /***
     * 댓글 신고 등록
     * @param report
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<Void> registCommentReport(@RequestBody ReportData report){

        System.out.println("@#$#@$@#$@#$#@$@#$ 요청 잘 들어옴");
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

    /***
     * 댓글신고 전체 조회
     * @param page
     * @param sortOrder
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Page<ReviewCommentReport> getReviewCommentReports(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "asc") String sortOrder) {
        return reviewCommentReportService.getAllReviewCommentReports(page, sortOrder);
    }

    /***
     * 댓글신고 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/detail/{reportId}")
    public ResponseEntity<Map<String, Object>> getReportDetail(@PathVariable("reportId") Long id) {

        Map<String, Object> response = new HashMap<>();

        ReviewCommentReport report = reviewCommentReportService.getReviewCommentReport(id);

        if (report != null) {
            response.put("report", report);
        } else {
            response.put("report", "report not found");
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{reportId}")
    public ResponseEntity<Void> updateReport(@PathVariable Long reportId, @RequestParam String btnId) {

        boolean isPunish = btnId.equals("commentPunish");

        reviewCommentReportService.reviewCommentReportPunish(reportId, isPunish);

        ReviewCommentReport reviewCommentReport = reviewCommentReportService.getReviewCommentReport(reportId);

        if(isPunish) {
            ReviewComment reviewComment = reviewCommentReport.getReviewComment();
            // 리뷰 상태값 변경 (제재 여부)
            reviewComment.setSuspended(true);

            reviewCommentService.saveComment(reviewComment);
            System.out.println(reviewComment.isSuspended());

            // 메일 발송 코드 예정
        } else {
            System.out.println("신고 처리 보류");
        }
        return ResponseEntity.ok().build(); // 명시적으로 상태 코드 200 OK를 반환
    }
}
