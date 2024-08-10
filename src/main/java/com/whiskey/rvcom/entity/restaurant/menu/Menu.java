package com.whiskey.rvcom.entity.restaurant.menu;

import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Table(name = "tbl_menu")
@Entity
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 메뉴 식별자

    @Column(nullable = false)
    private String name;    // 메뉴 이름

    @Column(nullable = false)
    private int price;  // 메뉴 가격(KRW)

    @OneToOne
    @JoinColumn(name = "menu_image_id")
    private ImageFile image;    // 메뉴 이미지

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;  // 해당 메뉴의 레스토랑
}