package com.whiskey.rvcom.review.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class ReviewCommentAddRequestModel {
    private Long reviewId;  // 리뷰 식별자
    private String content; // 댓글 내용
}