package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.RestaurantNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantNoticeRepository extends JpaRepository<RestaurantNotice, Long> {

     List<RestaurantNotice> findByRestaurantId(Long restaurantId);
}
