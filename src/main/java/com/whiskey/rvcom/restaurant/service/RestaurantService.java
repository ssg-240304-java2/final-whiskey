package com.whiskey.rvcom.restaurant.service;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.repository.MenuRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    public List<Restaurant> getNearbyRestaurantByLocation(double latitude, double longitude) {
        return restaurantRepository.findAllByDistance(latitude, longitude);
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public List<Menu> getMenuList(Long restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId);
    }
}
