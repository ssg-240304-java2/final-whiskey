package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOpenCloseTimeRepository extends JpaRepository<OpenCloseTime, Long> {
}
