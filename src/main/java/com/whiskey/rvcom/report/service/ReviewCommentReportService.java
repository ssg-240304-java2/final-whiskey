package com.whiskey.rvcom.report.service;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.entity.review.ReviewComment;
import com.whiskey.rvcom.repository.ReviewCommentReportRepository;
import com.whiskey.rvcom.repository.ReviewCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReviewCommentReportService {

    private final ModelMapper modelMapper;
    private final ReviewCommentReportRepository reviewCommentReportRepository;
    private final ReviewCommentRepository reviewCommentRepository;


    // 댓글 신고 전체 조회
    public Page<ReviewCommentReport> getAllReviewCommentReports(int page, String sortOrder) {

        Sort sort = Sort.by("reportedAt");

        if("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, 10, sort);
        return reviewCommentReportRepository.findAll(pageable);
    }


    // 댓글 신고 세부 조회
    public ReviewCommentReport getReviewCommentReport(Long id) {

        return reviewCommentReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReviewCommentReport not found with ID: " + id));
    }

    // 댓글 신고 상태값 변경
    @Transactional
    public void reviewCommentReportPunish(Long id, boolean isPunish) {

        ReviewCommentReport reviewCommentReport = reviewCommentReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReviewCommentReport not found with ID: " + id));

        reviewCommentReport.setChecked(true);

        if(isPunish) {
            reviewCommentReport.setVisible(false);
        }

        reviewCommentReportRepository.save(reviewCommentReport);
    }


    // 댓글 신고 등록
    @Transactional
    public void saveReviewCommentReport(ReviewCommentReport report) {

        ReviewCommentReport reviewCommentReport = modelMapper.map(report, ReviewCommentReport.class);

        ReviewComment reviewComment = report.getReviewComment();

        // 목록 조회 시 존재하지 않을 경우 예외를 던지고, 존재하면 영속성 주입시키기 위한 id
        Long reviewCommentId = reviewComment.getId();

        reviewComment = reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new EntityNotFoundException("ReviewComment not found" + reviewCommentId));

        reviewCommentReport.setReviewComment(reviewComment);

        reviewCommentReportRepository.save(reviewCommentReport);
    }

    public ReviewComment returnReviewComment(Long id) {

        return reviewCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReviewComment not found with ID: " + id));
    }
}
