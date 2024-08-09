package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.review.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
}
