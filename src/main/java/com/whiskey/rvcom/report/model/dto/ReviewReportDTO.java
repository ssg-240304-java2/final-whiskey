package com.whiskey.rvcom.report.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewReportDTO {

    private Long id;
    private boolean isChecked;
    private boolean isVisible;
    private LocalDateTime reportedAt;
    private ReviewDTO reviewDTO;
    private String content;
    private String title;

}
