package com.whiskey.rvcom.restaurant.dto;

import com.whiskey.rvcom.entity.restaurant.WeeklyOpenCloseTime;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RestaurantCardDTO {
    private Long id;
    private String name;
    private String category;
    private String distance;
    private String openCloseTime;
}
