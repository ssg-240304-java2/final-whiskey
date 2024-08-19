package com.whiskey.rvcom.inquiry.reply;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiryReply;
import com.whiskey.rvcom.inquiry.reply.dto.RestaurantInquiryReplyRequestDTO;
import com.whiskey.rvcom.repository.RestaurantInquiryReplyRepository;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback(value = false)
@Transactional
@SpringBootTest
class RestaurantInquiryReplyServiceTest {

    @Autowired
    private RestaurantInquiryReplyService inquiryReplyService;

    @Autowired
    private RestaurantInquiryReplyRepository inquiryReplyRepository;

    @Autowired
    private RestaurantInquiryRepository inquiryRepository;

    @Test
    void saveInquiryReply() {
        // given
        Long inquiryId = 5L;
        String replyContent = "네 추보빵입니다~~";

        RestaurantInquiryReplyRequestDTO request = new RestaurantInquiryReplyRequestDTO(inquiryId, replyContent);

        // when
        inquiryReplyService.save(request);

        // then
        RestaurantInquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));

        RestaurantInquiryReply reply = inquiry.getReply();

        assertThat(reply).isNotNull();
        assertEquals(replyContent, reply.getContent());
    }

    @Test
    void deleteInquiryReply() {
        // given
        Long replyId = 5L;

        // when
        inquiryReplyService.delete(replyId);

        // then
        RestaurantInquiryReply inquiryReply = inquiryReplyRepository.findById(replyId).orElseThrow();
        assertThat(inquiryReply.getDeletedAt()).isNotNull();
    }
}