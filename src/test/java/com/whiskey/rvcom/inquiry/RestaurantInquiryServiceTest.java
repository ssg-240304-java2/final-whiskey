package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(value = false)
@Transactional
@SpringBootTest
class RestaurantInquiryServiceTest {

    @Autowired
    private RestaurantInquiryService inquiryService;

    @Autowired
    private RestaurantInquiryRepository inquiryRepository;

    @Test
    void saveInquiry() {
        Long memberId = 5L;
        Long restaurantId = 2L;
        String content = "오늘의 메뉴 문의 남깁니다~~";
        RestaurantInquiryRequestDTO request = new RestaurantInquiryRequestDTO(content);

        inquiryService.save(restaurantId, request, memberId);

        RestaurantInquiry savedInquiry = inquiryRepository.findByContent(content)
                .orElseThrow(() -> new IllegalArgumentException("문의가 저장되지 않았습니다."));

        assertThat(savedInquiry).isNotNull();
        assertThat(savedInquiry.getContent()).isEqualTo(content);
        assertThat(savedInquiry.getWriter().getId()).isEqualTo(memberId);
    }

    @Test
    void findAllByRestaurantId() {
        // given
        Long restaurantId = 2L;

        // when
        List<RestaurantInquiry> inquiries = inquiryRepository.findAllByRestaurantId(restaurantId);

        inquiries.forEach(inquiry -> {
            System.out.println("음식점 " + inquiry.getRestaurant().getName());
            System.out.println("작성자 " + inquiry.getWriter().getName());
            System.out.println("문의 내용 " + inquiry.getContent());
            System.out.println("문의 작성일 " + inquiry.getCreatedAt());

            if (inquiry.getReply() == null) {
                System.out.println("답변이 없습니다.");
            } else {
                System.out.println("답변 내용 " + inquiry.getReply().getContent());
                System.out.println("답변 작성일" + inquiry.getReply().getCreatedAt());
            }
            System.out.println("-------------------------------------");
        });

        // then
        assertThat(inquiryRepository.findAllByRestaurantId(restaurantId))
                .isNotNull();
        assertThat(inquiryRepository.findAllByRestaurantId(restaurantId)
                .get(0)
                .getReply())
                .isNotNull();
    }

    @Test
    void deleteInquiry() {
        // given
        Long inquiryId = 11L;

        // when
        inquiryService.delete(inquiryId);

        // then
        assertThat(inquiryRepository.findById(inquiryId)
                .orElseThrow()
                .getDeletedAt())
                .isNotNull();
    }
}