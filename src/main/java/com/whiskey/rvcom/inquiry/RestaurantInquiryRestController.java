package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.report.model.dto.RestaurantInquiryResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestaurantInquiryRestController {
    private final RestaurantInquiryService inquiryService;

    @GetMapping("/restaurants/inquiries")
    public List<RestaurantInquiryResponseDTO> getInquiriesByRestaurantId() {
        return inquiryService.findInquiryByRestaurantId(2L);
    }

    // TODO: 문의 작성
//    @PostMapping("/restaurants/inquiries")
//    public void save(RestaurantInquiryReplyRequestDTO request, HttpSession session) {
//         TODO: session 에서 memberId 가져오기
//        Long memberId = (Long) session.getAttribute("memberId");
//        inquiryService.save(request, memberId);
//        inquiryService.save(request, 5L);
//    }
}
