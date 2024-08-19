package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
//    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final ReviewCommentService reviewCommentService;
    private final ReviewLikeService reviewLikeService;

    /*
        discuss.
         restaurantDetail페이지가 음식점 정보 / 리뷰 / 문의 등을 한 페이지에서 모두 해결하기 때문에 요청을 분산하지 않는 현행 구조에서는
         RestaurantController에서 핸들링 및 바인드해야 할 것으로 판단됨.
         대장님이랑 승인님과 논의 후 업무요청 전달 필요해 보임.
//     */
//    /**
//     * 리뷰 목록 조회 -> 리뷰 목록 뷰에 바인딩
//     * @param restaurantNo 대상 음식점 번호
//     * @return 리뷰 목록 페이지
//     */
//    @GetMapping("/list/{restaurantNo}")
//    public String getReviewListByRestaurantId(@PathVariable int restaurantNo, Model model) {
//        // todo. 음식점 번호로 리뷰 목록 조회
////        Long restaurantId = 1L; // Test Data
//        Restaurant restaurant = new Restaurant();   // block. id기반 엔티티 객체 검색 수행
//                                                    // need. 담당자 업무요청 전달
//
//        List<Review> reviews = reviewService.getReviewsByRestaurant(restaurant);
//        model.addAttribute("reviews", reviews);     // desc. 리뷰 목록 바인딩
//                                                                             // bind. 리뷰 목록 바인딩
//
//        // cpres. dest 리소스는 확정(변동여지 없음)
//        return "restaurantDetail";
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
//    @PostMapping("/comment")
    public String saveReviewComment(Long reviewNo, String content) { // need. 리뷰 작성자 세션 정보 추가 필요
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setCommenter(null);   // block. 로그인한 사용자 정보로 대체
        reviewComment.setContent(content);
        reviewComment.setReview(reviewService.getReviewById(reviewNo));

        return "redirect:/";
    }

    // 리뷰 댓글 삭제 요청
//    @PostMapping("/comment/remove")
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
