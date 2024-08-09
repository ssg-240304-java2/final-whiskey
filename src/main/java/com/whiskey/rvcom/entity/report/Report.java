package com.whiskey.rvcom.entity.report;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 신고 식별자

    @Column(nullable = false)
    private String title; // 신고 제목

    @Column(nullable = false)
    private String content;    // 신고 내용

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime reportedAt;   // 신고 시간

    @Column(nullable = false)
    private boolean isChecked;  // 신고 확인 여부

    @Column(nullable = false)
    private boolean isVisible;  // 공개 여부
}
