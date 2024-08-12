package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    /**
     * 특정 리뷰에 대응하는 댓글 리스트 조회
     * @param review 리뷰 객체
     * @return 댓글 리스트
     */
    public List<ReviewComment> getCommentsForReview(Review review) {
        return reviewCommentRepository.findByReview(review);
    }

    // 리뷰댓글의 id로 리뷰댓글 객체 조회

    /**
     * 리뷰댓글의 id로 리뷰댓글 객체 조회
     * @param commentId 리뷰댓글 id
     * @return 리뷰댓글 객체
     */
    public ReviewComment getCommentById(Long commentId) {
        return reviewCommentRepository.findById(commentId).orElseThrow(IllegalArgumentException::new);
    }
}
