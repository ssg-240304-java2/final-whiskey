package com.whiskey.rvcom.restaurant.service;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.WeeklyOpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.repository.MenuRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import com.whiskey.rvcom.restaurant.dto.RestaurantCardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    public List<RestaurantCardDTO> getNearbyRestaurantByLocation(double latitude, double longitude) {

        List<Restaurant> allByDistance = restaurantRepository.findAllByDistance(latitude, longitude);

        List<RestaurantCardDTO> restaurantCardList = new ArrayList<>();
        for (Restaurant restaurant : allByDistance) {
            restaurantCardList.add(convertToDTO(restaurant, latitude, longitude));
        }

        return restaurantCardList;
    }

    private RestaurantCardDTO convertToDTO(Restaurant restaurant, double latitude, double longitude) {

        double distance = 6371 * Math.acos(Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(restaurant.getAddress().getLatitude())) * Math.cos(Math.toRadians(restaurant.getAddress().getLongitude()) - Math.toRadians(longitude)) + Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(restaurant.getAddress().getLatitude())));
        distance = Math.round(distance * 100) / 100.0;

        String distanceString;
        if (distance > 1) {
            distanceString = distance + "km";
        } else {
            distanceString = distance * 1000 + "m";
        }

        String today = LocalDate.now().getDayOfWeek().toString();

        String openingHour = null;
        switch (today) {
            case "MONDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getMonday());
                break;
            case "TUESDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getTuesday());
                break;
            case "WEDNESDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getWednesday());
                break;
            case "THURSDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getThursday());
                break;
            case "FRIDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getFriday());
                break;
            case "SATURDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getSaturday());
                break;
            case "SUNDAY":
                openingHour = restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getSunday());
                break;
        }

        RestaurantCardDTO restaurantCardDTO = new RestaurantCardDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory().name(),
                distanceString,
                openingHour
        );

        return restaurantCardDTO;
    }

    private String restaurantTimeData(OpenCloseTime openCloseTime) {
        String hours;
        if (openCloseTime == null) {
            hours = "휴무";
        } else {
            hours = openCloseTime.getOpenTime() + " - " + openCloseTime.getCloseTime();
        }
        return hours;
    }

    public Map<String, Object> getRestaurantDetailById(Long id) {

        Map<String, Object> restaurantDetail = new HashMap<>();

        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        restaurantDetail.put("restaurant", restaurant);

        Map<String, String> todayOpeningHourAndStatus = restaurantTodayOpeningHourAndStatus(restaurant.getWeeklyOpenCloseTime());

        restaurantDetail.put("status", todayOpeningHourAndStatus.get("status"));
        restaurantDetail.put("hours", todayOpeningHourAndStatus.get("hours"));

        restaurantDetail.put("monday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getMonday()));
        restaurantDetail.put("tuesday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getTuesday()));
        restaurantDetail.put("wednesday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getWednesday()));
        restaurantDetail.put("thursday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getThursday()));
        restaurantDetail.put("friday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getFriday()));
        restaurantDetail.put("saturday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getSaturday()));
        restaurantDetail.put("sunday", restaurantWeeklyOpeningHour(restaurant.getWeeklyOpenCloseTime().getSunday()));

        return restaurantDetail;
    }

    private Map<String, String> restaurantTodayOpeningHourAndStatus(WeeklyOpenCloseTime weeklyOpenCloseTime) {

        String today = LocalDate.now().getDayOfWeek().toString();

        switch (today) {
            case "MONDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getMonday());
            case "TUESDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getTuesday());
            case "WEDNESDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getWednesday());
            case "THURSDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getThursday());
            case "FRIDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getFriday());
            case "SATURDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getSaturday());
            case "SUNDAY":
                return restaurantTodayStatus(weeklyOpenCloseTime.getSunday());
        }
        return null;
    }

    private Map<String, String> restaurantTodayStatus(OpenCloseTime openCloseTime) {
        Map<String, String> result = new HashMap<>();
        if (openCloseTime == null) {
            result.put("status", "미영업");
            result.put("hours", "");
        } else if (openCloseTime.isOpen()) {
            String openTime = openCloseTime.getOpenTime();
            String closeTime = openCloseTime.getCloseTime();
            LocalTime open = LocalTime.parse(openTime);
            LocalTime close = LocalTime.parse(closeTime);
            LocalTime now = LocalTime.now();
            if (now.isBefore(open) || now.isAfter(close)) {
                result.put("status", "영업종료");
            } else {
                result.put("status", "영업중");
            }
            result.put("hours", openCloseTime.getOpenTime() + " - " + openCloseTime.getCloseTime());
        } else {
            result.put("status", "미영업");
            result.put("hours", "");
        }
        return result;
    }

    private String restaurantWeeklyOpeningHour(OpenCloseTime openCloseTime) {
        if (openCloseTime != null) {
            return convert12HourFormat(openCloseTime.getOpenTime()) + " - " + convert12HourFormat(openCloseTime.getCloseTime());
        } else {
            return "미영업";
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

    public List<Menu> getMenuList(Long restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId);
    }
}
