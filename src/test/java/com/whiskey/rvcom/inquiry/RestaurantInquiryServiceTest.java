package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.report.model.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.repository.RestaurantInquiryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@Transactional
@SpringBootTest
class RestaurantInquiryServiceTest {

    @Autowired
    private RestaurantInquiryService inquiryService;

    @Autowired
    private RestaurantInquiryRepository inquiryRepository;

    @Test
    void inquirySave() {
        Long memberId = 5L;
        String content = "오늘의 메뉴 문의 남깁니다~~";
        RestaurantInquiryRequestDTO request = new RestaurantInquiryRequestDTO(2L, content);

        inquiryService.save(request, memberId);

        RestaurantInquiry savedInquiry = inquiryRepository.findByContent(content)
                .orElseThrow(() -> new IllegalArgumentException("문의가 저장되지 않았습니다."));

        assertThat(savedInquiry).isNotNull();
        assertThat(savedInquiry.getContent()).isEqualTo(content);
        assertThat(savedInquiry.getWriter().getId()).isEqualTo(memberId);
    }
}