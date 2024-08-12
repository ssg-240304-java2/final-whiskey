package com.whiskey.rvcom.report.model.dto;


import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private Long id;         // 음식점 식별자
    private String name;     // 음식점명
    private RestaurantCategory category; // 음식점 카테고리 (Enum 값을 문자열로 변환하여 사용)
    private String number;   // 음식점 전화번호
    private boolean isVisible; // 공개 여부
}
