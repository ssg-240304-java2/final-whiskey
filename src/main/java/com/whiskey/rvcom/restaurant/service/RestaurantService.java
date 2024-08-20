package com.whiskey.rvcom.restaurant.service;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getNearbyRestaurantByLocation(double latitude, double longitude) {
        return restaurantRepository.findAllByDistance(latitude, longitude);
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }
}
