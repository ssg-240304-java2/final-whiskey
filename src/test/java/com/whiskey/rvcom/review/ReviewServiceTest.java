package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.repository.RestaurantRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.MemberRepository;

@SpringBootTest
class ReviewServiceTest {
    private final ReviewService reviewService;
    private final RestaurantRepository restaurantRepository;
//    private final MemberRepository memberRepository;

    @Autowired
    public ReviewServiceTest(ReviewService reviewService, RestaurantRepository restaurantRepository) {
        this.reviewService = reviewService;
        this.restaurantRepository = restaurantRepository;
//        this.memberRepository = memberRepository;
    }

    @Test
    @DisplayName("특정 음식점에 리뷰 튜플 생성하여 저장")
    public void addNewReviewTest() {
        // 음식점 정보 영속화
        // TODO: 추후 Service단 모듈 활용하는 방향으로 조정
        Restaurant restaurant = restaurantRepository.findById(1L).orElseThrow(IllegalArgumentException::new);

        // 리뷰 작성자 정보 영속화
        // TODO. MemberRepository 패키지 위치변경 권고

        // 리뷰 객체 생성
        Review review = new Review();
        review.setRestaurant(restaurant);

    }
}