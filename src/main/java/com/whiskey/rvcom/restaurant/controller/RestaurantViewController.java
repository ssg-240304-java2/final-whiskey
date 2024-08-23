package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.restaurant.dto.RestaurantCardDTO;
import com.whiskey.rvcom.restaurant.dto.RestaurantSearchResultDTO;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class RestaurantViewController {

    private final RestaurantService restaurantService;

    public RestaurantViewController(RestaurantService restaurantService, ReviewService reviewService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurant/userLocationMain")
    @ResponseBody
    // main 페이지에서 사용자의 위치를 받아와 가까운 식당 정보를 반환
    public List<RestaurantCardDTO> userLocationMain(@RequestParam double lat, @RequestParam double lng) {
        return restaurantService.getNearbyRestaurantByLocation(lat, lng);
    }

    @GetMapping("/search-results")
    // 검색 결과 페이지로 이동
    public String searchResults(@RequestParam(value = "search", required = false) String searchText, Model model) {
        model.addAttribute("searchText", searchText);
        List<RestaurantSearchResultDTO> restaurantSearchResultList = restaurantService.getRestaurantsBySearchText(searchText);
        model.addAttribute("restaurantSearchResult", restaurantSearchResultList);
        return "searchResults";
    }
}