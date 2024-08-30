package com.whiskey.rvcom.restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class RestaurantOperatingHoursDTO {
    private Map<String, OperatingHourDTO> operatingHours;
}
