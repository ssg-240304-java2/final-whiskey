package com.whiskey.rvcom.restaurant.service;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.repository.MenuRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import com.whiskey.rvcom.restaurant.dto.RestaurantCardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public List<Menu> getMenuList(Long restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId);
    }
}
