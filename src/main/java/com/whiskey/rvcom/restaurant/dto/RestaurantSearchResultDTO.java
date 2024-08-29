package com.whiskey.rvcom.restaurant.dto;

import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import lombok.*;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
public class RestaurantSearchResultDTO {
    private Long id;
    private String name;
    private RestaurantCategory category;
    private Address address;

    private String bannerImageFileName;
    private String reviewAndRatingPhase;
    private int reviewCount;

    private String topReviewContent;

    public RestaurantSearchResultDTO(Long id, String name, RestaurantCategory category, Address address) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
    }
}
