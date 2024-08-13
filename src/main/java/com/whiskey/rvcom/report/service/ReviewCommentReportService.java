package com.whiskey.rvcom.report.service;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.entity.review.ReviewComment;
import com.whiskey.rvcom.report.model.dto.ReviewCommentDTO;
import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.repository.ReviewCommentReportRepository;
import com.whiskey.rvcom.repository.ReviewCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommentReportService {

    private final ModelMapper modelMapper;
    private final ReviewCommentReportRepository reviewCommentReportRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    public List<ReviewCommentReportDTO> reviewCommentReportDTOList = new ArrayList<>();

    // 댓글 신고 전체 조회
    public List<ReviewCommentReportDTO> getAllReviewCommentReports() {
        List<ReviewCommentReport> reports = reviewCommentReportRepository.findAll();

        for (ReviewCommentReport report : reports) {
            ReviewCommentDTO reviewCommentDTO = modelMapper.map(report.getReviewComment(), ReviewCommentDTO.class);
            ReviewCommentReportDTO reviewCommentReportDTO = modelMapper.map(report, ReviewCommentReportDTO.class);

            reviewCommentReportDTO.setReviewCommentDTO(reviewCommentDTO);
            reviewCommentReportDTOList.add(reviewCommentReportDTO);
        }
        return reviewCommentReportDTOList;
    }


    // 댓글 신고 등록
    @Transactional
    public void saveReviewCommentReport(ReviewCommentReportDTO report) {

        ReviewCommentReport reviewCommentReport = modelMapper.map(report, ReviewCommentReport.class);

        ReviewCommentDTO reviewCommentDTO = report.getReviewCommentDTO();

        Long reviewCommentId = reviewCommentDTO.getId();

        ReviewComment reviewComment = reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new EntityNotFoundException("ReviewComment not found" + reviewCommentId));

        reviewCommentReport.setReviewComment(reviewComment);

        reviewComment = modelMapper.map(reviewCommentDTO, ReviewComment.class);
        reviewCommentReport.setReviewComment(reviewComment);

        reviewCommentReportRepository.save(reviewCommentReport);
    }

}
