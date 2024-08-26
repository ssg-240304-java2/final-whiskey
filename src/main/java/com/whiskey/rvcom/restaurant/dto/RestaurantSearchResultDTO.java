package com.whiskey.rvcom.restaurant.dto;

import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RestaurantSearchResultDTO {
    private Long id;
    private String name;
    private RestaurantCategory category;
    private Address address;
}
