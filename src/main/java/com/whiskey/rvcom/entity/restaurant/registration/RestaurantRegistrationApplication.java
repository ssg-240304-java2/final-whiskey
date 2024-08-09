package com.whiskey.rvcom.entity.restaurant.registration;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "tbl_restaurant_registration_application")
@Entity
public class RestaurantRegistrationApplication {
    // TODO: 입점 신청서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;    // 신청서 식별자

    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;  // 음식점명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantCategory restaurantCategory; // 음식점 카테고리

    @Column(nullable = false)
    private String restaurantNumber;  // 음식점 전화번호

    @ManyToOne
    @JoinColumn(name = "member_id",  nullable = false)
    private Member member;  // 입점 신청한 회원

    @ManyToOne
    @JoinColumn(name = "restaurant_address_id", nullable = false)
    private Address restaurantAddress;  // 음식점 주소

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;    // 생성 시점에 현재 일시로 초기화

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false)
    private RegistrationStatus registrationStatus;  // 신청서 등록 상태
}
