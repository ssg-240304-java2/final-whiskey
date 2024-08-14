package com.whiskey.rvcom.inquiry.dto;

import java.time.LocalDateTime;

public record RestaurantInquiryResponseDTO(
        String content,
        String writer,
        LocalDateTime createdAt,
        RestaurantInquiryReplyResponseDTO reply
) {
}
