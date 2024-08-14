package com.whiskey.rvcom.entity.review;

import com.whiskey.rvcom.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_review_comment")
@Entity
@Getter
@Setter
public class ReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 리뷰 댓글 식별자

    @Column(nullable = false)
    private String content; // 리뷰 댓글 내용

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 리뷰 댓글 작성일

    private LocalDateTime deletedAt;    // 리뷰 댓글 삭제일

    @Column(nullable = false)
    private boolean isSuspended;    // 노출 여부

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;  // 리뷰 객체의 정보

    @ManyToOne
    @JoinColumn(name = "commenter_id", nullable = false)
    private Member commenter;   // 리뷰 댓글 작성자
}