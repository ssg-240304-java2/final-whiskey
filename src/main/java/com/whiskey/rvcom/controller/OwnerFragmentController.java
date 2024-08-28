package com.whiskey.rvcom.controller;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.ReviewService;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class OwnerFragmentController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @GetMapping("/fragments/owner/{fragment}")
    public String getOwnerFragment(@PathVariable String fragment) {
        return "fragments/owner/" + fragment;
    }

    @GetMapping("/owner-dashboard")
    public String ownerDashboard(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        System.out.println("로그인된 사용자의 정보: " + member);

        if (member == null) {
            return "redirect:/login";
        }

        String profileImageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/049/273/cover11.jpg";
        if (member.getProfileImage() != null) {
            profileImageUrl = ImagePathParser.parse(member.getProfileImage().getUuidFileName());
        }

        Long restaurantId = restaurantService.getRestaurantByOwnerId(member.getId()).getId();
        System.out.println("로그인된 사용자의 음식점 ID: " + restaurantId);

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        System.out.println("로그인된 사용자의 음식점 정보: " + restaurant);

        var reviews = reviewService.getReviewsByRestaurantAsList(restaurant);
        System.out.println("리뷰 리스트: " + reviews);

        int reviewCount = reviews.size();       // 총 리뷰 수 바인딩용
        model.addAttribute("reviewCount", reviewCount); // todo. 총 리뷰 수 바인딩
        System.out.println("총 리뷰 수: " + reviewCount);

        double avgRating = reviewService.getAverageRatingForRestaurant(reviews); // 평균 별점 도출
//        model.addAttribute("avgRating", avgRating);
        // avgRating / 5 형태로 출력되도록
        String ratingPhase = avgRating + " / 5";
        model.addAttribute("avgRating", ratingPhase);   // todo. 평균평점 바인딩

        // 리뷰 목록에서 최근순으로 3개만 가져오기
        var recentReviews = reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(3)
                .toList();
        model.addAttribute("recentReviews", recentReviews); // todo. 최근 리뷰 목록 바인딩

        // 최근 일주일(7일)간의 리뷰 수 도출
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        int weeklyReviewCount = (int) reviews.stream()
                .filter(review -> review.getCreatedAt().isAfter(oneWeekAgo))
                .count();
        model.addAttribute("weeklyReviewCount", weeklyReviewCount); // todo. 최근 리뷰 수 바인딩
        System.out.println("최근 일주일간의 리뷰 수: " + weeklyReviewCount);

        // 인기 리뷰 수 도출하여 바인딩
        var copiedReviews = reviews.stream().toList();
        copiedReviews.sort((r1, r2) -> r2.getLikes().size() - r1.getLikes().size());
        var topReviews = copiedReviews.stream().limit(3).toList();
        model.addAttribute("topReviews", topReviews); // todo. 인기 리뷰 목록 바인딩

        model.addAttribute("profileImageUrl", profileImageUrl);
        model.addAttribute("memberName", member.getName());
        model.addAttribute("restaurantId", restaurantId);

        return "owner/dashboard";
    }
}
