package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAddressRepository extends JpaRepository<Address, Long> {
}
