package com.whiskey.rvcom.inquiry.reply;

import com.whiskey.rvcom.inquiry.reply.dto.RestaurantInquiryReplyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RestaurantInquiryReplyRestController {
    private final RestaurantInquiryReplyService inquiryReplyService;

    // TODO: 문의 답변 작성
    @PostMapping("/restaurant/inquiry/reply")
    public void save(RestaurantInquiryReplyRequestDTO request) {
        inquiryReplyService.save(request);
    }

    // TODO: 문의 답변 삭제
    @DeleteMapping("/restaurant/inquiry/reply")
    public void delete(Long replyId) {
        inquiryReplyService.delete(replyId);
    }
}
