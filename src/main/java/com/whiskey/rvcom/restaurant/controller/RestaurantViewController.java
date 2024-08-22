package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.restaurant.dto.RestaurantCardDTO;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/restaurant")
public class RestaurantViewController {

    private final RestaurantService restaurantService;

    public RestaurantViewController(RestaurantService restaurantService, ReviewService reviewService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/userLocationMain")
    @ResponseBody
    public List<RestaurantCardDTO> userLocationMain(@RequestParam double lat, @RequestParam double lng) {
        return restaurantService.getNearbyRestaurantByLocation(lat, lng);
    }

    @GetMapping("/search-results")
    // 검색 결과 페이지로 이동
    public String searchResults(@RequestParam(value = "searchText", required = false) String searchText, Model model) {
        model.addAttribute("searchText", searchText);
        return "searchResults";
    }
}