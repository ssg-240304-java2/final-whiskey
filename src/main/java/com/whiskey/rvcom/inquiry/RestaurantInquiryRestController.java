package com.whiskey.rvcom.inquiry;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryRequestDTO;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryResponseDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/restaurant/{restaurantId}/user-inquiries")
    public List<RestaurantInquiryResponseDTO> findAllByRestaurantId(@PathVariable Long restaurantId) {
        return inquiryService.findAllByRestaurantId(restaurantId);
    }

    /**
     * 음식점의 문의글 조회하기(답변 포함, 페이지)
     * @param restaurantId
     * @return 문의글
     */
    @GetMapping("/restaurant/{restaurantId}/owner-inquiries")
    public Page<RestaurantInquiry> getPagedRestaurantInquiries(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "1" ) int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return inquiryService.getPagedRestaurantInquiries(restaurantId, pageRequest);
    }

    /**
     * 문의 상세 보기
     * @param inquiryId
     * @return 문의글 상세 보기
     */
    @GetMapping("/restaurant/{inquiryId}/inquiry")
    public RestaurantInquiryResponseDTO findById(@PathVariable Long inquiryId) {
        return inquiryService.findById(inquiryId);
    }

    /**
     * 미답변 문의글 수 조회
     * @param restaurantId
     * @return 미답변 문의글 수
     */
    @GetMapping("/restaurant/{restaurantId}/inquiries/unanswered-count")
    public ResponseEntity<Integer> getUnansweredInquiryCount(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(inquiryService.getUnansweredInquiryCount(restaurantId));
    }

    /**
     * 문의 작성
     * @param restaurantId
     * @param request
     * @param session
     */
    @PostMapping("/restaurant/{restaurantId}/inquiry")
    public void save(@PathVariable Long restaurantId, @RequestBody RestaurantInquiryRequestDTO request, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        inquiryService.save(restaurantId, request, member);
    }

    /**
     * 문의 삭제
     * @param inquiryId
     */
    @DeleteMapping("/restaurant/{inquiryId}/inquiry")
    public void delete(@PathVariable Long inquiryId) {
        inquiryService.delete(inquiryId);
    }

    /**
     * 회원 정보 조회
     * @param session
     * @return 회원 정보
     */
    @GetMapping("/restaurant/inquiry/member")
    public ResponseEntity<Member> getMember(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        return ResponseEntity.ok(member);
    }
}
