package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

        return restaurantService.getNearbyRestaurantByLocation(lat, lng);
    }

    @GetMapping("/detail/{restaurantId}")
    public String restaurantDetail(Model model, @PathVariable long restaurantId) {

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        model.addAttribute("restaurant", restaurant);

        String today = LocalDate.now().getDayOfWeek().toString();

        switch (today) {
            case "MONDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getMonday(), model);
                break;
            case "TUESDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getTuesday(), model);
                break;
            case "WEDNESDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getWednesday(), model);
                break;
            case "THURSDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getThursday(), model);
                break;
            case "FRIDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getFriday(), model);
                break;
            case "SATURDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getSaturday(), model);
                break;
            case "SUNDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getSunday(), model);
                break;
        }
        return "restaurantDetail";
    }

    private void restaurantTimeData(OpenCloseTime openCloseTime, Model model) {
        String hours;
        if (openCloseTime == null) {
            model.addAttribute("status", "미영업");
            hours = "";
        } else if (openCloseTime.isOpen()) {
            String openTime = openCloseTime.getOpenTime();
            String closeTime = openCloseTime.getCloseTime();
            LocalTime open = LocalTime.parse(openTime);
            LocalTime close = LocalTime.parse(closeTime);
            LocalTime now = LocalTime.now();
            if (now.isBefore(open) || now.isAfter(close)) {
                model.addAttribute("status", "영업종료");
            } else {
                model.addAttribute("status", "영업중");
            }
            hours = openCloseTime.getOpenTime() + " - " + openCloseTime.getCloseTime();
        } else {
            model.addAttribute("status", "미영업");
            hours = "";
        }
        model.addAttribute("hours", hours);
    }
}
