package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/restaurant/review")
@RequiredArgsConstructor
public class ReviewController {
    private final RestaurantService restaurantService;
//    private final RestaurantRepository restaurantRepository;    // need. 서비스 모듈로 교체 필요(업요전달)

    private final ReviewService reviewService;
    private final ReviewCommentService reviewCommentService;
    private final ReviewLikeService reviewLikeService;
    private final ReviewImageService reviewImageService;

    // use path variable
    // 리뷰 목록 조회 요청
//    @GetMapping("/{restaurantNo}")
//    public String getReviewsByRestaurantId(@PathVariable Long restaurantNo, Model model) {
//        Restaurant restaurant = restaurantRepository.findById(restaurantNo).orElseThrow();
//        List<Review> reviews = reviewService.getReviewsByRestaurant(restaurant);
//
//        model.addAttribute("reviews", reviews);     // desc. 리뷰 목록 바인딩
//
//        return "restaurantDetail";  // need. 뷰 분할 후 리뷰 페이지에 대한 뷰로 변경
//    }

    //    @PostMapping("/list/{restaurantNo}")
    public String getReviewCommentsByReviewId(@PathVariable Long reviewNo, Model model) {
        // todo. 리뷰 아이디로 리뷰 댓글 목록 조회
        Review dest = reviewService.getReviewById(reviewNo);
        List<ReviewComment> comments = reviewCommentService.getCommentsForReview(dest);

        model.addAttribute("comments", comments);     // desc. 리뷰 댓글 목록 바인딩
        return "restaurantDetail";
    }

    // 리뷰 댓글 작성 요청
    @PostMapping("/comment")
    public String saveReviewComment(Long reviewNo, String content) { // need. 리뷰 작성자 세션 정보 추가 필요
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setCommenter(null);   // block. 로그인한 사용자 정보로 대체
        reviewComment.setContent(content);
        reviewComment.setReview(reviewService.getReviewById(reviewNo));

        return "redirect:/";
    }

    // 리뷰 댓글 삭제 요청
    @PostMapping("/comment/remove")
    public String removeReviewComment(Long commentNo) {
        ReviewComment dest = reviewCommentService.getCommentById(commentNo);
        reviewCommentService.removeComment(dest);

        return "redirect:/";
    }

    // 리뷰 좋아요 추가 요청
//    @PostMapping("/like")
    public void addLikeToReview(Long reviewNo) { // need. 좋아요 처리할 사용자 정보 추가 필요
        Review dest = reviewService.getReviewById(reviewNo);

        Member member = new Member();   // block. 로그인한 사용자 정보로 대체
        ReviewLike reviewLike = reviewLikeService.getReviewLikeByReviewAndMember(dest, member);

        // 이미 해당 리뷰에 좋아요를 누른 경우 좋아요 취소(토글처리)
        if (reviewLike == null) {
            reviewLike = new ReviewLike();
            reviewLike.setReview(dest);
            reviewLike.setMember(member);
            reviewLikeService.addReviewLike(reviewLike);
        } else {
            reviewLikeService.removeReviewLike(reviewLike);
        }
    }
}
