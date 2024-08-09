package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
//    List<Review> findByProductId(Long productId);
}
