package com.whiskey.rvcom.entity.review;

import com.whiskey.rvcom.entity.member.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "tbl_review_like")
@Entity
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 리뷰 좋아요 식별자

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;  // 리뷰 좋아요 선택한 회원
}
