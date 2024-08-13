package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 아이디로 리뷰 객체 조회
     * @param reviewId 리뷰 아이디
     * @return 리뷰 엔티티 객체
     */
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 회원 객체로 리뷰 리스트 조회
     * @param member 회원 객체
     * @return 리뷰 리스트
     */
    public List<Review> getReviewsByMember(Member member) {
        return reviewRepository.findByReviewer(member);
    }

    // TODO. Resource 담당자쪽 service에 구현되도록 업무요청 전달 -> 컨트롤러 모듈에서 받아 처리할 예정
    // DESC. 2024/08/11 19:27 - 업무요청 완료

    /**
     * 리뷰 작성 / 수정
     * @param review 리뷰 객체
     */
    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }
}
