package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantInquiryRepository extends JpaRepository<RestaurantInquiry, Long> {

    @Query("""
        select i from RestaurantInquiry i
        where i.restaurant.id = :restaurantId
        """)
    List<RestaurantInquiry> findInquiryByRestaurantId(Long restaurantId);

}
