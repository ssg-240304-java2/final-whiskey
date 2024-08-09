package com.whiskey.rvcom.entity.resource;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_image_file")
@Entity
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String originalFileName;    // 원본 파일명

    @Column(nullable = false)
    private String uuidFileName;    // UUID 파일명

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime uploadedAt;    // 업로드 시점에 현재 일시로 초기화
}