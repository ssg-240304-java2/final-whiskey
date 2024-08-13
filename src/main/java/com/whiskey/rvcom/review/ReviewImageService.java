package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    public ReviewImage getReviewImageById(Long reviewImageId) {
        return reviewImageRepository.findById(reviewImageId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 리뷰 엔티티 객체로 리뷰 이미지 리스트 조회
     * @param review 리뷰 엔티티 객체
     * @return 리뷰 이미지 리스트
     */
    public List<ReviewImage> getReviewImagesByReview(Review review) {
        return reviewImageRepository.findByReview(review);
    }

    /**
     * 리뷰 이미지 저장
     * @param reviewImage 리뷰 이미지 객체
     */
    @Transactional
    public void addReviewImage(ReviewImage reviewImage) {
        reviewImageRepository.save(reviewImage);
    }

    /**
     * 리뷰 이미지 삭제
     * @param reviewImage 리뷰 이미지 객체
     */
    @Transactional
    public void deleteReviewImage(ReviewImage reviewImage) {
        reviewImageRepository.delete(reviewImage);
    }
}
