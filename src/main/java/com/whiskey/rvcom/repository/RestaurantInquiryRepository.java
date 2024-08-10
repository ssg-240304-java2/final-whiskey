package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantInquiryRepository extends JpaRepository<RestaurantInquiry, Long> {
}
