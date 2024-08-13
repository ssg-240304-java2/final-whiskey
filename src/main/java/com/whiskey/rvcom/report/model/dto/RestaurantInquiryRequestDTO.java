package com.whiskey.rvcom.report.model.dto;

public record RestaurantInquiryRequestDTO(
        Long restaurantId,
        String content
) {
}
