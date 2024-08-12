package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.review.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReview(Review review);
}
