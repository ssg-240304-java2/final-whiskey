package com.whiskey.rvcom.report.service;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.entity.review.ReviewComment;
import com.whiskey.rvcom.review.dto.ReviewCommentDTO;
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


    // 댓글 신고 전체 조회
    public List<ReviewCommentReportDTO> getAllReviewCommentReports() {
        List<ReviewCommentReportDTO> reviewCommentReportDTOList = new ArrayList<>();

        List<ReviewCommentReport> reports = reviewCommentReportRepository.findAll();

        for (ReviewCommentReport report : reports) {
            ReviewCommentDTO reviewCommentDTO = modelMapper.map(report.getReviewComment(), ReviewCommentDTO.class);
            ReviewCommentReportDTO reviewCommentReportDTO = modelMapper.map(report, ReviewCommentReportDTO.class);

            reviewCommentReportDTO.setReviewCommentDTO(reviewCommentDTO);
            reviewCommentReportDTOList.add(reviewCommentReportDTO);
        }
        return reviewCommentReportDTOList;
    }


    // 댓글 신고 세부 조회
    public ReviewCommentReportDTO getReviewCommentReport(Long id) {

        ReviewCommentReport reviewCommentReport = reviewCommentReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReviewCommentReport not found with ID: " + id));

        ReviewCommentReportDTO result = modelMapper.map(reviewCommentReport, ReviewCommentReportDTO.class);
        result.setReviewCommentDTO(modelMapper.map(reviewCommentReport.getReviewComment(), ReviewCommentDTO.class));

        return result;
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
    public void saveReviewCommentReport(ReviewCommentReportDTO report) {

        ReviewCommentReport reviewCommentReport = modelMapper.map(report, ReviewCommentReport.class);

        ReviewCommentDTO reviewCommentDTO = report.getReviewCommentDTO();

        // 목록 조회 시 존재하지 않을 경우 예외를 던지고, 존재하면 영속성 주입시키기 위한 id
        Long reviewCommentId = reviewCommentDTO.getId();

        ReviewComment reviewComment = reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new EntityNotFoundException("ReviewComment not found" + reviewCommentId));

        reviewCommentReport.setReviewComment(reviewComment);

        reviewComment = modelMapper.map(reviewCommentDTO, ReviewComment.class);
        reviewCommentReport.setReviewComment(reviewComment);

        reviewCommentReportRepository.save(reviewCommentReport);
    }

    public ReviewCommentDTO returnReviewCommentDTO(Long id) {

        ReviewComment reviewComment = reviewCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReviewComment not found with ID: " + id));

        return modelMapper.map(reviewComment, ReviewCommentDTO.class);
    }
}
