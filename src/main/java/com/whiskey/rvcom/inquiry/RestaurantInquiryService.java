package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryReplyResponseDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryResponseDTO;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantInquiryService {
    private final RestaurantInquiryRepository inquiryRepository;
    private final RestaurantRepository restaurantRepository;

    public List<RestaurantInquiryResponseDTO> findAllByRestaurantId(Long restaurantId) {
        return inquiryRepository.findAllByRestaurantId(restaurantId).stream()
                .sorted(Comparator.comparing(RestaurantInquiry::getCreatedAt).reversed())
                .map(it -> new RestaurantInquiryResponseDTO(
                        it.getId(),
                        it.getContent(),
                        it.getWriter().getId(),
                        it.getWriter().getName(),
                        it.getCreatedAt(),
                        it.getReply() != null ?
                                new RestaurantInquiryReplyResponseDTO(
                                it.getReply().getContent(),
                                it.getReply().getCreatedAt()
                        ) : null
                )).toList();
    }

    public Page<RestaurantInquiry> getPagedRestaurantInquiries(Long restaurantId, Pageable pageable) {
        return inquiryRepository.getPagedRestaurantInquiries(restaurantId, pageable);
    }

    public int getUnansweredInquiryCount(Long restaurantId) {
        return inquiryRepository.countUnansweredInquiries(restaurantId);
    }

    @Transactional
    public void save(Long restaurantId, RestaurantInquiryRequestDTO request, Member member) {
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        inquiryRepository.save(new RestaurantInquiry(member, restaurant, request.content()));
    }

    @Transactional
    public void delete(Long inquiryId) {
        RestaurantInquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));
        inquiry.delete();
        inquiryRepository.save(inquiry);
    }

    public RestaurantInquiryResponseDTO findById(Long inquiryId) {
        inquiryRepository.findById(inquiryId).orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));
        return inquiryRepository.findById(inquiryId)
                .map(it -> new RestaurantInquiryResponseDTO(
                    it.getId(),
                    it.getContent(),
                    it.getWriter().getId(),
                    it.getWriter().getName(),
                    it.getCreatedAt(),
                    it.getReply() != null ?
                            new RestaurantInquiryReplyResponseDTO(
                                    it.getReply().getContent(),
                                    it.getReply().getCreatedAt()
                            ) : null
        )).orElseThrow();
    }
}
