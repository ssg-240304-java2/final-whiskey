package com.whiskey.rvcom.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReportDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime reportedAt;
    private boolean isChecked;
    private boolean isVisible;
    private RestaurantDTO restaurantDTO;
}
