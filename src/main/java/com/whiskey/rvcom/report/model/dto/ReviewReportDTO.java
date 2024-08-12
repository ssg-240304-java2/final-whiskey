package com.whiskey.rvcom.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReportDTO {

    private Long id;
    private boolean isChecked;
    private boolean isVisible;
    private LocalDateTime reportedAt;
    private ReviewDTO reviewDTO;
    private String content;
    private String title;

}
