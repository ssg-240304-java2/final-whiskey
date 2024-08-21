package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByRestaurantId(Long restaurantId);
}
