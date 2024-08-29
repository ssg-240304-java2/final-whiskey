package com.whiskey.rvcom.review;

import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.receipt.ReceiptData;
import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.repository.*;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReceiptService receiptService;
    private final ImageFileService imageFileService;
    private final RestaurantService restaurantService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);


    /**
     * 리뷰 아이디로 리뷰 객체 조회
     *
     * @param reviewId 리뷰 아이디
     * @return 리뷰 엔티티 객체
     */
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 회원 객체로 리뷰 리스트 조회
     *
     * @param member 회원 객체
     * @return 리뷰 리스트
     */
    public List<Review> getReviewsByMember(Member member) {
        return reviewRepository.findByReviewer(member);
    }

    /**
     * 음식점 객체로 리뷰 리스트 조회
     *
     * @param restaurant 음식점 객체
     * @return 리뷰 리스트
     */
    public Map<String, Object> getReviewsByRestaurant(Restaurant restaurant) {
        List<Review> reviewsByRestaurant = reviewRepository.findByRestaurant(restaurant);

        reviewsByRestaurant.sort((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));

        // review 각각에서 isSuspended가 false인 review만 가져오기
        reviewsByRestaurant.removeIf(review -> review.isSuspended());

        double rating = getAverageRatingForRestaurant(reviewsByRestaurant);
        StringBuilder ratingPhase = new StringBuilder();

        // 2.5 -> ★★☆☆☆
        // 지금 2.5에 3개가 나옴. 즉, 버림처리하도록 바꿔야 함.
        int ratingInt = (int) rating;
        for (int i = 0; i < 5; i++) {
            if (i < ratingInt) {
                ratingPhase.append("★");
            } else {
                ratingPhase.append("☆");
            }
        }
        ratingPhase.append(" ").append(rating);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("ratingPhase", ratingPhase.toString());
        attributes.put("reviews", reviewsByRestaurant);

        return attributes;
    }

    // override with params. return type = List<Review>
    public List<Review> getReviewsByRestaurantAsList(Restaurant restaurant) {
        return reviewRepository.findByRestaurant(restaurant);
    }

    // TODO. Resource 담당자쪽 service에 구현되도록 업무요청 전달 -> 컨트롤러 모듈에서 받아 처리할 예정
    // DESC. 2024/08/11 19:27 - 업무요청 완료

    /**
     * 리뷰 작성 / 수정
     *
     * @param review 리뷰 객체
     */
    @Transactional
    public Review saveReview(Review review) {
        reviewRepository.save(review);
        return review;
    }

    /**
     * 리뷰 삭제
     *
     * @param review 리뷰 객체(반드시 영속성 상태의 객체가 전달되어야 함)
     */
    @Transactional
    public void removeReview(Review review) {
        reviewRepository.delete(review);
    }

//    /**
//     * 리뷰에 좋아요 추가
//     * @param review 리뷰 객체
//     * @param reviewLike 리뷰 좋아요 객체
//     */
//    @Transactional
//    public void addLikeToReview(Review review, ReviewLike reviewLike) {
//        review.getLikes().add(reviewLike);
//    }

    /**
     * 음식점 당 총 Review-Rating 값의 평균치를 계산하여 반환
     *
     * @param reviews 음식점 리뷰 리스트
     * @return 음식점에 대한 리뷰 평균 평점
     */
    public double getAverageRatingForRestaurant(List<Review> reviews) {
        return reviews.stream()
                .mapToInt(review -> review.getRating().getValue())
                .average()
                .orElse(0);
    }

    @Transactional
    public Review saveReview(ReviewDTO reviewDTO, List<MultipartFile> images, Member member) throws Exception {
        // ReceiptData 가져오기
        ReceiptData receiptData = receiptService.getReceipt(reviewDTO.getReceiptDataId());
        if (receiptData == null) {
            logger.error("영수증 데이터를 찾을 수 없습니다. ID: {}", reviewDTO.getReceiptDataId());
            throw new IllegalArgumentException("유효하지 않은 영수증 데이터입니다. ID: " + reviewDTO.getReceiptDataId());
        }

        // Restaurant 가져오기
        Restaurant restaurant = restaurantService.getRestaurantById(reviewDTO.getRestaurantId());
        if (restaurant == null) {
            throw new IllegalArgumentException("유효하지 않은 레스토랑 ID입니다.");
        }

        // Review 엔티티 생성 및 설정
        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setContent(reviewDTO.getContent());
        review.setRestaurant(restaurant);
        review.setReviewer(member);
        review.setReceiptData(receiptData);

        // 이미지 처리
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile image : images) {
            ImageFile imageFile = imageFileService.uploadFile(image);
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setReview(review);
            reviewImage.setImageFile(imageFile);
            reviewImages.add(reviewImage);
        }
        review.setReviewImages(reviewImages);

        // 리뷰 저장
        return reviewRepository.save(review);
    }

}
