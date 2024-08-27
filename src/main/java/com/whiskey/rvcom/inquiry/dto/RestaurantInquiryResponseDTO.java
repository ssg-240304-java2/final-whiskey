package com.whiskey.rvcom.inquiry.dto;

import java.time.LocalDateTime;

public record RestaurantInquiryResponseDTO(
        Long id,
        String content,
        Long writerId,
        String writer,
        LocalDateTime createdAt,
        RestaurantInquiryReplyResponseDTO reply
) {
}
