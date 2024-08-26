package com.whiskey.rvcom.inquiry.reply;

import com.whiskey.rvcom.inquiry.reply.dto.RestaurantInquiryReplyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RestaurantInquiryReplyRestController {
    private final RestaurantInquiryReplyService inquiryReplyService;

    // TODO: 문의 답변 작성
    @PostMapping("/restaurant/inquiry/reply/{inquiryId}")
    public void save(@PathVariable Long inquiryId, @RequestBody RestaurantInquiryReplyRequestDTO request) {
        inquiryReplyService.save(inquiryId, request);
    }

    // TODO: 문의 답변 삭제
    @DeleteMapping("/restaurant/inquiry/reply")
    public void delete(Long replyId) {
        inquiryReplyService.delete(replyId);
    }
}
