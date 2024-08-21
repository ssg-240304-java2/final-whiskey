package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryReplyResponseDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryResponseDTO;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantInquiryService {
    private final RestaurantInquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    // TODO: 음식점의 문의글 조회하기
    public List<RestaurantInquiryResponseDTO> findAllByRestaurantId(Long restaurantId) {
        return inquiryRepository.findAllByRestaurantId(restaurantId).stream()
                .map(it -> new RestaurantInquiryResponseDTO(
                        it.getContent(),
                        it.getWriter().getName(),
                        it.getCreatedAt(),
                        new RestaurantInquiryReplyResponseDTO(
                                it.getContent(),
                                it.getCreatedAt()
                        ))).toList();
    }

    // TODO: 문의 작성
    @Transactional
    public void save(RestaurantInquiryRequestDTO request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        Restaurant restaurant = restaurantRepository.getReferenceById(request.restaurantId());
        inquiryRepository.save(new RestaurantInquiry(member, restaurant, request.content()));
    }

    // TODO: 문의 삭제
    @Transactional
    public void delete(Long inquiryId) {
        RestaurantInquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));
        inquiry.delete();
        inquiryRepository.save(inquiry);
    }
}
