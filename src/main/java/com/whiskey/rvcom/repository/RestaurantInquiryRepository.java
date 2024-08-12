package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantInquiryRepository extends JpaRepository<RestaurantInquiry, Long> {
    List<RestaurantInquiry> findByRestaurantId(Long restaurantId);
}
