package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
}
