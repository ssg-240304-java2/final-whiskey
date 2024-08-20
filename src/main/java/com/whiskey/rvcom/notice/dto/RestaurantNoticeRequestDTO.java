package com.whiskey.rvcom.notice.dto;

public record RestaurantNoticeRequestDTO(
        Long restaurantId,
        String title,
        String content
) {
}
