package com.whiskey.rvcom.inquiry.dto;

public record RestaurantInquiryRequestDTO(
        Long restaurantId,
        String content
) {
}
