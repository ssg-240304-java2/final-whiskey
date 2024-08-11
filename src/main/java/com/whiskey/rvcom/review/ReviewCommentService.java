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

    public List<ReviewComment> getCommentsForReview(Review review) {
        return reviewCommentRepository.findByReview(review);
    }
}
