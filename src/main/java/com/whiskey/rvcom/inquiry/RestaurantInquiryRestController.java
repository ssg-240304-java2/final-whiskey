package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryResponseDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestaurantInquiryRestController {
    private final RestaurantInquiryService inquiryService;

    /**
     * 음식점의 문의글 조회하기(답변 포함)
     * @param restaurantId
     * @return 문의글
     */
    @GetMapping("/restaurant/{restaurantId}/inquiry")
    public List<RestaurantInquiryResponseDTO> findAllByRestaurantId(@PathVariable Long restaurantId) {
        return inquiryService.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/restaurant/inquiry/{inquiryId}")
    public RestaurantInquiryResponseDTO findById(@PathVariable Long inquiryId) {
        return inquiryService.findById(inquiryId);
    }

    /**
     * 문의 작성
     * @param restaurantId
     * @param request
     * @param session
     */
    @PostMapping("/restaurant/{restaurantId}/inquiry")
    public void save(@PathVariable Long restaurantId, @RequestBody RestaurantInquiryRequestDTO request, HttpSession session) {
//         TODO: session 에서 memberId 가져오기
         Member memberId = (Member) session.getAttribute("memberId");
//        inquiryService.save(request, memberId);
        inquiryService.save(restaurantId, request, 5L);
    }

    /**
     * 문의 삭제
     * @param inquiryId
     */
    @DeleteMapping("/restaurant/{inquiryId}/inquiry")
    public void delete(@PathVariable Long inquiryId) {
        inquiryService.delete(inquiryId);
    }
}
