package com.whiskey.rvcom.report.service;

import com.whiskey.rvcom.entity.report.ReviewCommentReport;
import com.whiskey.rvcom.report.model.dto.ReviewCommentDTO;
import com.whiskey.rvcom.report.model.dto.ReviewCommentReportDTO;
import com.whiskey.rvcom.repository.ReviewCommentReportRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommentReportService {

    private final ModelMapper modelMapper;
    private final ReviewCommentReportRepository reviewCommentReportRepository;

    public List<ReviewCommentReportDTO> reviewCommentReportDTOList = new ArrayList<>();

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

}
