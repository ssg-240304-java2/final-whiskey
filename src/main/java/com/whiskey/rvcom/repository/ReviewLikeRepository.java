package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.review.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByReviewAndMember(Review review, Member member);
}
