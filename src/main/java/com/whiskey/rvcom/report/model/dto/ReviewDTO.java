package com.whiskey.rvcom.report.model.dto;

import com.whiskey.rvcom.entity.review.Rating;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {

    private Long id;
    private boolean isSuspended;
    private LocalDateTime createdAt;
    private long restaurantId;
    private long reviewerId;
    private String content;
    private Rating rating;
}
