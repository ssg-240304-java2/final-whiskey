package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.report.model.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantInquiryReplyResponseDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantInquiryResponseDTO;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantInquiryService {
    private final RestaurantInquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public List<RestaurantInquiryResponseDTO> findInquiryByRestaurantId(Long restaurantId) {
        return inquiryRepository.findInquiryByRestaurantId(restaurantId).stream()
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
    public void save(RestaurantInquiryRequestDTO request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        Restaurant restaurant = restaurantRepository.getReferenceById(request.restaurantId());
        inquiryRepository.save(new RestaurantInquiry(member, restaurant, request.content()));
    }
}
