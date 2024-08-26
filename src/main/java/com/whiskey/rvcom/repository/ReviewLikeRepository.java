package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.review.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByReviewAndMember(Review review, Member member);

    List<ReviewLike> findByReviewId(Long id);
}
