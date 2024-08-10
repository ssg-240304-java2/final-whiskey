package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.WeeklyOpenCloseTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantWeeklyOpenCloseTimeRepository extends JpaRepository<WeeklyOpenCloseTime, Long> {
}
