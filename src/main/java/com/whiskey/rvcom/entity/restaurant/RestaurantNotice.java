package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_restaurant_notice")
@Entity
@Getter
@Setter
public class RestaurantNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 음식점 공지사항 식별자

    @Column(nullable = false)
    private String title;  // 음식점 공지사항 제목

    @Column(nullable = false)
    private String content;    // 음식점 공지사항 내용

    @Column(nullable = false)
    private boolean isDeleted;  // 음식점 공지사항 삭제 여부

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 음식점 공지사항 작성시간

    private LocalDateTime deletedAt;    // 음식점 공지사항 삭제시간

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;  // 음식점 객체의 정보

    public RestaurantNotice(String title, String content, Restaurant restaurant) {
        this.title = title;
        this.content = content;
        this.restaurant = restaurant;
    }
}