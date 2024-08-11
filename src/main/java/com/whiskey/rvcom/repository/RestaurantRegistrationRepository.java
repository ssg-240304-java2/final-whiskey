package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.registration.RestaurantRegistrationApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRegistrationRepository extends JpaRepository<RestaurantRegistrationApplication, Long> {
}
