package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.repository.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;

    /**
     * 리뷰와 회원으로 리뷰 좋아요 객체 조회
     * @param review 리뷰 객체
     * @param member 회원 객체
     * @return 리뷰 좋아요 객체
     */
    public ReviewLike getReviewLikeByReviewAndMember(Review review, Member member) {
        return reviewLikeRepository.findByReviewAndMember(review, member);
    }

    /**
     * 리뷰 좋아요 저장
     * @param reviewLike 리뷰 좋아요 객체
     */
    public void addReviewLike(ReviewLike reviewLike) {
        reviewLikeRepository.save(reviewLike);
    }

    /**
     * 리뷰 좋아요 삭제
     * @param reviewLike 리뷰 좋아요 객체
     */
    public void removeReviewLike(ReviewLike reviewLike) {
        reviewLikeRepository.delete(reviewLike);
    }
}