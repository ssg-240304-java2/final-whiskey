package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_open_close_time")
@Entity
@Getter
@Setter
public class OpenCloseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String openTime;

    private String closeTime;

    //    영업중 여부
    @Column(nullable = false)
    private boolean isOpen;
}