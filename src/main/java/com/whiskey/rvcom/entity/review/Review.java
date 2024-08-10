package com.whiskey.rvcom.entity.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Table(name = "tbl_review")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 리뷰 식별자

    // TODO 데이터베이스에 Value 값이 저장되게 Converter 어노테이션과 클래스 생성해야함
    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;  // 별점

    @Column(nullable = false)
    private String content; // 리뷰 내용

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 리뷰 작성일

    private LocalDateTime deletedAt;    // 리뷰 삭제일

    @Column(nullable = false)
    private boolean isSuspended; // 노출 여부

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;  // 음식점 객체의 정보

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Member reviewer;    // 리뷰 작성자

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id")
    private List<ReviewLike> likes; // 좋아요 리스트
}