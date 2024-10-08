package com.whiskey.rvcom.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.whiskey.rvcom.entity.resource.ImageFile;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Table(name = "review_image")
@Entity
@Getter
@Setter
@ToString(exclude = "review")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 리뷰 이미지 식별자

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;  // 리뷰

    @ManyToOne
    @JoinColumn(name = "image_file_id", nullable = false)
    private ImageFile imageFile;    // 이미지파일
}
