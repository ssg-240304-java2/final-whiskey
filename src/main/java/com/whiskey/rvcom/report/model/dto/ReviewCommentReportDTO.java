package com.whiskey.rvcom.report.model.dto;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ReviewCommentReportDTO {

    private Long id;
    private boolean isChecked;
    private boolean isVisible;
    private LocalDateTime reportedAt;
    private ReviewCommentDTO reviewCommentDTO;
    private String content;
    private String title;
}
