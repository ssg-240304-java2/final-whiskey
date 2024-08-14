package com.whiskey.rvcom.notice;

import com.whiskey.rvcom.notice.dto.RestaurantNoticeRequestDTO;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO;
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
class RestaurantNoticeServiceTest {

    @Autowired
    private RestaurantNoticeService noticeService;

    @Test
    void findNoticeByRestaurantId() {
        // given
        Long restaurantId = 2L;

        // when
        List<RestaurantNoticeResponseDTO> findNotices = noticeService.findNoticeByRestaurantId(restaurantId);

        // then
        assertThat(findNotices).isNotNull();
    }

    @Test
    void saveNotice() {
        // given
        Long restaurantId = 2L;
        String title = "오늘의 메뉴 안내";
        String content = "오늘의 메뉴는 돈까스입니다. 많은 관심 부탁드립니다.";
        RestaurantNoticeRequestDTO request = new RestaurantNoticeRequestDTO(restaurantId, title, content);

        // when
        noticeService.save(request, restaurantId);

        List<RestaurantNoticeResponseDTO> findNotices = noticeService.findNoticeByRestaurantId(restaurantId);

        // then
        assertThat(findNotices.get(findNotices.size() - 1).title()).isEqualTo(title);
    }
}