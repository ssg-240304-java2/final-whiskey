package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.WeeklyOpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.restaurant.dto.RestaurantCardDTO;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/restaurant")
public class RestaurantViewController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    public RestaurantViewController(RestaurantService restaurantService, ReviewService reviewService) {
        this.restaurantService = restaurantService;
        this.reviewService = reviewService;
    }

    @GetMapping("/userLocationMain")
    @ResponseBody
    public List<RestaurantCardDTO> userLocationMain(@RequestParam double lat, @RequestParam double lng) {
        return restaurantService.getNearbyRestaurantByLocation(lat, lng);
    }

    @GetMapping("/{restaurantId}/{tab}")
    public String getRestaurantDetailWithTab(Model model, @PathVariable Long restaurantId, @PathVariable String tab) {
        // TODO: restaurantId와 tab에 따른 데이터 로딩 로직 구현
        // TODO: 모델에 restaurantId와 tab 정보 추가
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantId", restaurantId);

        model.addAttribute("tab", tab); // 저 탭과 관련한 내용은 PathVariable이 아닌 상수 info 로~

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

        restaurantWeeklyOpeningTime(model, restaurant.getWeeklyOpenCloseTime());

        List<Menu> menuList = restaurantService.getMenuList(restaurantId);
        model.addAttribute("menuList", menuList);

        // 리뷰 관련 정보
        Map<String, Object> reviewAttributes = reviewService.getReviewsByRestaurant(restaurant);

        model.addAttribute("ratingPhase", reviewAttributes.get("ratingPhase"));
        model.addAttribute("reviews", reviewAttributes.get("reviews"));

        return "restaurantDetail";
    }

    private void restaurantWeeklyOpeningTime(Model model, WeeklyOpenCloseTime weeklyOpenCloseTime) {
        if (weeklyOpenCloseTime.getMonday() != null) {
            model.addAttribute("monday", convert12HourFormat(weeklyOpenCloseTime.getMonday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getMonday().getCloseTime()));
        } else {
            model.addAttribute("monday", "미영업");
        }

        if (weeklyOpenCloseTime.getTuesday() != null) {
            model.addAttribute("tuesday", convert12HourFormat(weeklyOpenCloseTime.getTuesday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getTuesday().getCloseTime()));
        } else {
            model.addAttribute("tuesday", "미영업");
        }

        if (weeklyOpenCloseTime.getWednesday() != null) {
            model.addAttribute("wednesday", convert12HourFormat(weeklyOpenCloseTime.getWednesday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getWednesday().getCloseTime()));
        } else {
            model.addAttribute("wednesday", "미영업");
        }

        if (weeklyOpenCloseTime.getThursday() != null) {
            model.addAttribute("thursday", convert12HourFormat(weeklyOpenCloseTime.getThursday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getThursday().getCloseTime()));
        } else {
            model.addAttribute("thursday", "미영업");
        }

        if (weeklyOpenCloseTime.getFriday() != null) {
            model.addAttribute("friday", convert12HourFormat(weeklyOpenCloseTime.getFriday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getFriday().getCloseTime()));
        } else {
            model.addAttribute("friday", "미영업");
        }

        if (weeklyOpenCloseTime.getSaturday() != null) {
            model.addAttribute("saturday", convert12HourFormat(weeklyOpenCloseTime.getSaturday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getSaturday().getCloseTime()));
        } else {
            model.addAttribute("saturday", "미영업");
        }

        if (weeklyOpenCloseTime.getSunday() != null) {
            model.addAttribute("sunday", convert12HourFormat(weeklyOpenCloseTime.getSunday().getOpenTime()) + " - " + convert12HourFormat(weeklyOpenCloseTime.getSunday().getCloseTime()));
        } else {
            model.addAttribute("sunday", "미영업");
        }
    }

    private String convert12HourFormat(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        String minute = time.split(":")[1];

        if (hour >= 12) {
            return hour + ":" + minute + " PM";
        } else {
            return hour + ":" + minute + " AM";
        }
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