package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewer(Member reviewer);

    List<Review> findByRestaurant(Restaurant restaurant);
//    List<Review> findByProductId(Long productId);
}
