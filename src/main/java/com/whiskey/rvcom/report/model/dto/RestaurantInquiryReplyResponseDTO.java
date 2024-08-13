package com.whiskey.rvcom.report.model.dto;

import java.time.LocalDateTime;

public record RestaurantInquiryReplyResponseDTO(
        String content,
        LocalDateTime createdAt
) {
}
