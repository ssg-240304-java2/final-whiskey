package com.whiskey.rvcom.restaurant.dto;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnersRestaurantInfoDTO {
    private String name;
    private String number;
    private String address;
    private String ownerName;
    private String category;

    private String imageUrl;
    private Map<String, String> operatingHours;
}
