package com.whiskey.rvcom.inquiry.reply;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiryReply;
import com.whiskey.rvcom.inquiry.reply.dto.RestaurantInquiryReplyRequestDTO;
import com.whiskey.rvcom.repository.RestaurantInquiryReplyRepository;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RestaurantInquiryReplyService {
    private final RestaurantInquiryRepository inquiryRepository;
    private final RestaurantInquiryReplyRepository inquiryReplyRepository;

    // TODO: 문의글에 답변 작성
    @Transactional
    public void save(Long inquiryId, RestaurantInquiryReplyRequestDTO request) {
        RestaurantInquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));

        RestaurantInquiryReply reply = new RestaurantInquiryReply(request.content());

        inquiryReplyRepository.save(reply);

        inquiry.addReply(reply);
        inquiryRepository.save(inquiry);
    }

    // TODO: 문의글에 답변 삭제
    @Transactional
    public void delete(Long replyId) {
        RestaurantInquiry inquiry = inquiryRepository.findByReplyId(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));
        RestaurantInquiryReply inquiryReply = inquiryReplyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글에 답변이 존재하지 않습니다."));

        inquiry.addReply(null);
        inquiryRepository.save(inquiry);

        inquiryReply.delete();
        inquiryReplyRepository.save(inquiryReply);
    }
}
