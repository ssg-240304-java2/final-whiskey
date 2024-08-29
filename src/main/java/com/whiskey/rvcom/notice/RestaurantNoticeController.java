package com.whiskey.rvcom.notice;

import com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestaurantNoticeController {
    private final RestaurantNoticeService noticeService;

    /**
     * 해당 음식점의 공지사항 전체 조회
     * @param restaurantId
     * @return 음식점 공지사항 리스트
     */
    @GetMapping("/restaurant/{restaurantId}/user-notice")
    public List<RestaurantNoticeResponseDTO> findNoticeByRestaurantId(@PathVariable Long restaurantId) {
        return noticeService.findNoticeByRestaurantId(restaurantId);
    }
}
