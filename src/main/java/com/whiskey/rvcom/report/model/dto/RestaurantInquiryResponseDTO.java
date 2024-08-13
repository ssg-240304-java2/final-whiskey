package com.whiskey.rvcom.report.model.dto;

import java.time.LocalDateTime;

public record RestaurantInquiryResponseDTO(
        String content,
        String writer,
        LocalDateTime createdAt,
        RestaurantInquiryReplyResponseDTO reply
) {
}
