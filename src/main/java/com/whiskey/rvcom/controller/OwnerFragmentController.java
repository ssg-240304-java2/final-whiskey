package com.whiskey.rvcom.controller;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.ReviewService;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.whiskey.rvcom.inquiry.RestaurantInquiryService;


import java.time.LocalDateTime;
import java.util.List;

/**
 * 레스토랑 소유자를 위한 대시보드 및 관련 기능을 제공하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class OwnerFragmentController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final RestaurantInquiryService restaurantInquiryService;

    /**
     * 소유자 대시보드의 특정 프래그먼트를 로드하는 메서드
     * @param fragment 로드할 프래그먼트 이름
     * @param model Spring MVC 모델
     * @param session HTTP 세션
     * @return 프래그먼트 뷰 이름
     */
    @GetMapping("/fragments/owner/{fragment}")
    public String getOwnerFragment(@PathVariable String fragment, Model model, HttpSession session) {
        if ("dashboard".equals(fragment)) {
            loadDashboardData(model, session);
        }
        return "fragments/owner/" + fragment;
    }

    /**
     * 소유자 대시보드 메인 페이지를 로드하는 메서드
     * @param session HTTP 세션
     * @param model Spring MVC 모델
     * @return 대시보드 뷰 이름 또는 로그인 페이지로의 리다이렉트
     */
    @GetMapping("/owner-dashboard")
    public String ownerDashboard(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login";
        }

        String profileImageUrl = getProfileImageUrl(member);
        Long restaurantId = restaurantService.getRestaurantByOwnerId(member.getId()).getId();

        loadDashboardData(model, session);

        model.addAttribute("profileImageUrl", profileImageUrl);
        model.addAttribute("memberName", member.getName());
        model.addAttribute("restaurantId", restaurantId);

        return "owner/dashboard";
    }

    /**
     * 대시보드에 필요한 데이터를 로드하는 private 메서드
     * @param model Spring MVC 모델
     * @param session HTTP 세션
     */
    private void loadDashboardData(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Restaurant restaurant = restaurantService.getRestaurantByOwnerId(member.getId());
        var reviews = reviewService.getReviewsByRestaurantAsList(restaurant);

        int reviewCount = reviews.size();
        double avgRating = reviewService.getAverageRatingForRestaurant(reviews);
        String ratingPhase = String.format("%.1f / 5", avgRating);
        var recentReviews = getRecentReviews(reviews);
        int weeklyReviewCount = getWeeklyReviewCount(reviews);
        int unansweredInquiryCount = restaurantInquiryService.getUnansweredInquiryCount(restaurant.getId());


        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("avgRating", ratingPhase);
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("weeklyReviewCount", weeklyReviewCount);
        model.addAttribute("unansweredInquiryCount", unansweredInquiryCount); 
    }

    /**
     * 회원의 프로필 이미지 URL을 가져오는 private 메서드
     * @param member 회원 객체
     * @return 프로필 이미지 URL
     */
    private String getProfileImageUrl(Member member) {
        return member.getProfileImage() != null
                ? ImagePathParser.parse(member.getProfileImage().getUuidFileName())
                : "https://i.kym-cdn.com/entries/icons/facebook/000/049/273/cover11.jpg";
    }

    /**
     * 최근 리뷰 3개를 가져오는 private 메서드
     * @param reviews 전체 리뷰 리스트
     * @return 최근 리뷰 3개 리스트
     */
    private List<Review> getRecentReviews(List<Review> reviews) {
        return reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(3)
                .toList();
    }

    /**
     * 지난 7일간의 리뷰 수를 계산하는 private 메서드
     * @param reviews 전체 리뷰 리스트
     * @return 주간 리뷰 수
     */
    private int getWeeklyReviewCount(List<Review> reviews) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        return (int) reviews.stream()
                .filter(review -> review.getCreatedAt().isAfter(oneWeekAgo))
                .count();
    }
}
