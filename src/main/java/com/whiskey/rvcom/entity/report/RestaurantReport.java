package com.whiskey.rvcom.entity.report;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Table(name = "tbl_restaurant_report")
@Entity
@Getter
@Setter
public class RestaurantReport extends Report {
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}