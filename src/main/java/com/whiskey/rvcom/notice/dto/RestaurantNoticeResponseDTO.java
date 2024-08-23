package com.whiskey.rvcom.notice.dto;

import java.time.LocalDateTime;

public record RestaurantNoticeResponseDTO(
        Long id,
        String title,
        String content,
        Boolean isDeleted,
        LocalDateTime createdAt
) {
}
