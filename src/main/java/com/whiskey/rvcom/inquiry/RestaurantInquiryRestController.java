package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryResponseDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestaurantInquiryRestController {
    private final RestaurantInquiryService inquiryService;

    // TODO: 문의 작성
    @PostMapping("/restaurants/inquiries")
    public void save(RestaurantInquiryRequestDTO request, HttpSession session) {
//         TODO: session 에서 memberId 가져오기
         Member memberId = (Member) session.getAttribute("memberId");
//        inquiryService.save(request, memberId);
        inquiryService.save(request, 5L);
    }

    // TODO: 음식점의 문의글 조회하기(답변 포함)
    @GetMapping("/restaurant/{restaurantId}/inquiries")
    public List<RestaurantInquiryResponseDTO> findAllByRestaurantId(@PathVariable Long restaurantId) {
        return inquiryService.findAllByRestaurantId(restaurantId);
    }

    // TODO: 문의 삭제
    @DeleteMapping("/restaurants/inquiries")
    public void delete(Long inquiryId) {
        inquiryService.delete(inquiryId);
    }
}
