package com.whiskey.rvcom.report.service;

import com.whiskey.rvcom.entity.report.ReviewReport;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.review.dto.ReviewDTO;
import com.whiskey.rvcom.report.model.dto.ReviewReportDTO;
import com.whiskey.rvcom.repository.ReviewReportRepository;
import com.whiskey.rvcom.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewReportService {

    private final ModelMapper modelMapper;

    private final ReviewReportRepository reviewReportRepository;

    private final ReviewRepository reviewRepository;



    // 리뷰 신고 전체 조회
    public List<ReviewReportDTO> getAllReviewReports() {

        List<ReviewReportDTO> reviewReportDTOList = new ArrayList<>();

        List<ReviewReport> reports = reviewReportRepository.findAll();

        for (ReviewReport report : reports) {
            ReviewDTO reviewDTO = modelMapper.map(report.getReview(), ReviewDTO.class);
            ReviewReportDTO reviewReportDTO = modelMapper.map(report, ReviewReportDTO.class);

            reviewReportDTO.setReviewDTO(reviewDTO);
            reviewReportDTOList.add(reviewReportDTO);
        }
        return reviewReportDTOList;
    }


    // 리뷰 신고 등록
    @Transactional
    public void saveReviewReport(ReviewReportDTO report) {

        ReviewReport reviewReport = modelMapper.map(report, ReviewReport.class);

        ReviewDTO reviewDTO = report.getReviewDTO();

        Long reviewId = reviewDTO.getId();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        reviewReport.setReview(review);

        review = modelMapper.map(reviewDTO, Review.class);
        reviewReport.setReview(review);

        reviewReportRepository.save(reviewReport);


    }


}
