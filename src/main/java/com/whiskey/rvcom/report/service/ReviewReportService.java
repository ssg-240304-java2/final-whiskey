package com.whiskey.rvcom.report.service;

import com.whiskey.rvcom.entity.report.ReviewReport;
import com.whiskey.rvcom.report.model.dto.ReviewDTO;
import com.whiskey.rvcom.report.model.dto.ReviewReportDTO;
import com.whiskey.rvcom.repository.ReviewReportRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewReportService {

    private final ModelMapper modelMapper;
    private final ReviewReportRepository reviewReportRepository;

    public List<ReviewReportDTO> reviewReportDTOList = new ArrayList<>();

    public List<ReviewReportDTO> getAllReviewReports() {
        List<ReviewReport> reports = reviewReportRepository.findAll();

        for (ReviewReport report : reports) {
            ReviewDTO reviewDTO = modelMapper.map(report.getReview(), ReviewDTO.class);
            ReviewReportDTO reviewReportDTO = modelMapper.map(report, ReviewReportDTO.class);

            reviewReportDTO.setReviewDTO(reviewDTO);
            reviewReportDTOList.add(reviewReportDTO);
        }
        return reviewReportDTOList;
    }


}
