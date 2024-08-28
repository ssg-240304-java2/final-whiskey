package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.registration.RegistrationStatus;
import com.whiskey.rvcom.entity.restaurant.registration.RestaurantRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRegistrationRepository extends JpaRepository<RestaurantRegistration, Long> {
    Page<RestaurantRegistration> findByRegistrationStatus(Pageable pageable, RegistrationStatus registrationStatus);
}
