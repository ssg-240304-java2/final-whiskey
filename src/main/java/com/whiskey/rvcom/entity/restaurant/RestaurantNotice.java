package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_restaurant_notice")
@Entity
public class RestaurantNotice {
    // TODO 여러개가 있으면 문의하기 형태
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 음식점 공지사항 식별자

    @Column(nullable = false)
    private String title;  // 음식점 공지사항 제목

    @Column(nullable = false)
    private String content;    // 음식점 공지사항 내용

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 음식점 공지사항 작성시간

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;  // 음식점 객체의 정보
}