package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/restaurant")
public class RestaurantViewController {

    private final RestaurantService restaurantService;

    public RestaurantViewController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/userLocationMain")
    @ResponseBody
    public List<Restaurant> userLocationMain(@RequestParam double lat, @RequestParam double lng) {

        return restaurantService.getRestaurantByUserLocation(lat, lng);
    }
}
