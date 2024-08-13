package com.whiskey.rvcom.report.model.dto;

public record RestaurantInquiryReplyRequestDTO(
        Long restaurantId,
        String content
) {
}
