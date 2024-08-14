package com.whiskey.rvcom.inquiry.dto;

import java.time.LocalDateTime;

public record RestaurantInquiryReplyResponseDTO(
        String content,
        LocalDateTime createdAt
) {
}
