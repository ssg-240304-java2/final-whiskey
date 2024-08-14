package com.whiskey.rvcom.review.dto;

import com.whiskey.rvcom.entity.review.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentDTO {

    private Long id;
    private boolean isSuspended;
    private LocalDateTime createdAt;
    private long restaurantId;
    private long reviewerId;
    private String content;
    private Rating rating;

}
