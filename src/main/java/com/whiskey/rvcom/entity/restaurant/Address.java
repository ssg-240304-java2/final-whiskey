package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * Reference
 * https://developers.kakao.com/docs/latest/ko/local/dev-guide
 */
@NoArgsConstructor
@Table(name = "tbl_address")
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 주소 식별자

    @Column(nullable = false)
    private String name;    // 주소

    @Column(name = "latitude", nullable = false)
    private double latitude;    // 위도

    @Column(name = "longitude", nullable = false)
    private double longitude;    // 경도
}