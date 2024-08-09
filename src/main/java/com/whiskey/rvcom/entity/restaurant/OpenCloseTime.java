package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "tbl_open_close_time")
@Entity
public class OpenCloseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String openTime;

    @Column(nullable = false)
    private String closeTime;

//    영업중 여부
    @Column(nullable = false)
    private boolean isOpen;
}