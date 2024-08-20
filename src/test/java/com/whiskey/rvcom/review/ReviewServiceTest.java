package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.repository.*;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewServiceTest {
    private final ReviewService reviewService;
    private final ReviewCommentService reviewCommentService;
    private final RestaurantRepository restaurantRepository;

    private final MemberRepository memberRepository;
//    private final MemberRepository memberRepository;

    @Autowired
    public ReviewServiceTest(ReviewService reviewService, RestaurantRepository restaurantRepository, MemberRepository memberRepository, ReviewCommentService reviewCommentService) {
        this.reviewService = reviewService;
        this.restaurantRepository = restaurantRepository;
        this.memberRepository = memberRepository;
        this.reviewCommentService = reviewCommentService;
    }

    @Test
    @DisplayName("특정 음식점에 리뷰 튜플 생성하여 저장 테스트")
    public void addNewReviewTest() {
        // 영속 상태의 Restaurant 객체 취득
        Restaurant restaurant = restaurantRepository.findById(2L).orElseThrow(IllegalArgumentException::new);

        // 영속 상태의 Member 객체 취득
        Member member = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);

        // 리뷰 객체 생성
        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setRating(Rating.FOUR_STAR);
        review.setContent("맛있어요!");
        review.setReviewer(member);

        // 리뷰 저장
        Review returnedReview = reviewService.saveReview(review);

        // 리뷰 저장 확인
        Assertions.assertEquals(review, returnedReview);
    }

    @Test
    @DisplayName("리뷰에 리뷰 댓글 튜플 생성하여 저장 테스트")
    public void addReviewCommentTest() {
        Review review = reviewService.getReviewById(5L);

        Member member = memberRepository.findById(6L).orElseThrow(IllegalArgumentException::new);
        // 7~8번도 사용?

        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setReview(review);
        reviewComment.setCommenter(member);
        reviewComment.setContent("맛있어요!라니 ... 맛 없는데 ㅠ");

        ReviewComment returnedReviewComment = reviewCommentService.saveComment(reviewComment);

        Assertions.assertEquals(reviewComment, returnedReviewComment);
    }

}