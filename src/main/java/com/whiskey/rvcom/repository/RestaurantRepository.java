package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r order by ( 6371 * acos( cos( radians(:lat) ) * cos( radians( r.address.latitude ) ) * cos( radians( r.address.longitude ) - radians(:lng) ) + sin( radians(:lat) ) * sin( radians( r.address.latitude ) ) ) ) ASC")
    List<Restaurant> findAllByDistance(double lat, double lng);
}
