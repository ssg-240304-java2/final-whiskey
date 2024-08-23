package com.whiskey.rvcom.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.whiskey.rvcom.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Table(name = "tbl_review_like")
@Entity
@Getter
@Setter
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 리뷰 좋아요 식별자

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;  // 리뷰 좋아요 선택한 리뷰

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;  // 리뷰 좋아요 선택한 회원
}
