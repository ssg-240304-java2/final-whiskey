package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.RestaurantNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantNoticeRepository extends JpaRepository<RestaurantNotice, Long> {
}
