package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMember(Member reviewer);
//    List<Review> findByProductId(Long productId);
}
