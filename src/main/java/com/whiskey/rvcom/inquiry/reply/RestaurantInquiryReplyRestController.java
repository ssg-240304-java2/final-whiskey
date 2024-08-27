package com.whiskey.rvcom.inquiry.reply;

import com.whiskey.rvcom.inquiry.reply.dto.RestaurantInquiryReplyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RestaurantInquiryReplyRestController {
    private final RestaurantInquiryReplyService inquiryReplyService;

    /**
     * 문의 답변 작성
     * @param inquiryId
     * @param request
     */
    @PostMapping("/restaurant/inquiry/reply/{inquiryId}")
    public void save(@PathVariable Long inquiryId, @RequestBody RestaurantInquiryReplyRequestDTO request) {
        inquiryReplyService.save(inquiryId, request);
    }
}
