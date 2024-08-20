package com.whiskey.rvcom.notice.dto;

import java.time.LocalDateTime;

public record RestaurantNoticeResponseDTO(
        String title,
        String content,
        LocalDateTime createdAt
) {
}
