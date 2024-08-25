package com.whiskey.rvcom.notice;

import com.whiskey.rvcom.notice.dto.RestaurantNoticeRequestDTO;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestaurantOwnerNoticeController {
    private final RestaurantNoticeService noticeService;

    // 음식점의 공지사항 전체 조회
    @GetMapping("/restaurant/{restaurantId}/allNotice")
    public Page<RestaurantNoticeResponseDTO> getPagedRestaurantNotices(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return noticeService.getPagedRestaurantNotices(restaurantId, pageRequest);
    }

    /**
     * 해당 음식점의 점주가 공지사항 작성
     * @param restaurantId
     * @param request
     */
    @PostMapping("/restaurant/{restaurantId}/notice")
    public void save(@PathVariable Long restaurantId, @RequestBody RestaurantNoticeRequestDTO request) {
        noticeService.save(request, restaurantId);
    }

    /**
     * 해당 음식점의 점주가 공지사항 삭제
     * @param noticeId
     */
    @DeleteMapping("/restaurant/{noticeId}/notice")
    public void delete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
    }
}
