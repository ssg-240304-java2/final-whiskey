package com.whiskey.rvcom.inquiry.reply.dto;

public record RestaurantInquiryReplyRequestDTO(
        Long inquiryId,
        String content
) {
}
