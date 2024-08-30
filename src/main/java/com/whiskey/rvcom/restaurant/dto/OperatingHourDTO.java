package com.whiskey.rvcom.restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OperatingHourDTO {
    private Boolean isOpen;
    private String open;
    private String close;
}
