package com.whiskey.rvcom.entity.inquiry.restaurant;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_restaurant_inquiry_reply")
@Entity
@Getter
@Setter
public class RestaurantInquiryReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 문의 답변 식별자

    @Column(nullable = false)
    private String content; // 문의 답변 내용

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 문의 답변 작성일

    private LocalDateTime deletedAt;    // 문의 답변 삭제일
}