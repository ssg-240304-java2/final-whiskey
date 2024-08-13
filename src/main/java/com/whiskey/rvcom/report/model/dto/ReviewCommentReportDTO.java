package com.whiskey.rvcom.report.model.dto;


import com.whiskey.rvcom.review.dto.ReviewCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentReportDTO {

    private Long id;
    private boolean isChecked;
    private boolean isVisible;
    private LocalDateTime reportedAt;
    private ReviewCommentDTO reviewCommentDTO;
    private String content;
    private String title;
}
