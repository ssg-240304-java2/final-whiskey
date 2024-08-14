package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.registration.RestaurantRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRegistrationRepository extends JpaRepository<RestaurantRegistration, Long> {
}
