package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.RestaurantNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantNoticeRepository extends JpaRepository<RestaurantNotice, Long> {

     @Query("""
             SELECT n FROM RestaurantNotice n
             WHERE n.restaurant.id = :restaurantId and
                n.isDeleted = false
             """)
     List<RestaurantNotice> findByRestaurantId(Long restaurantId);
}
