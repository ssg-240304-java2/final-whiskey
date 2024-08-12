package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByAddress(Address address);

    boolean existsByName(String name);
}
