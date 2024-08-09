package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "tbl_restaurant_category")
@Entity
public class RestaurantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 카테고리 식별자

    // TODO: KAKAO API 에서 카테고리 받아오면 저장
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;    // 카테고리명
}