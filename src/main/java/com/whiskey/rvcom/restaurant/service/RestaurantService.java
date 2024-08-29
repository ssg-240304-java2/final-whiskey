package com.whiskey.rvcom.restaurant.service;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.WeeklyOpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.repository.MenuRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import com.whiskey.rvcom.restaurant.dto.OperatingHourDTO;
import com.whiskey.rvcom.restaurant.dto.RestaurantCardDTO;
import com.whiskey.rvcom.restaurant.dto.RestaurantSearchResultDTO;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final EntityManager entityManager;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuRepository menuRepository, EntityManager entityManager) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.entityManager = entityManager;
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

        WeeklyOpenCloseTime weeklyOpenCloseTime = restaurant.getWeeklyOpenCloseTime();

        String openingHour = null;
        switch (today) {
            case "MONDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getMonday() : null);
                break;
            case "TUESDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getTuesday() : null);
                break;
            case "WEDNESDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getWednesday() : null);
                break;
            case "THURSDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getThursday() : null);
                break;
            case "FRIDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getFriday() : null);
                break;
            case "SATURDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getSaturday() : null);
                break;
            case "SUNDAY":
                openingHour = restaurantTimeData(weeklyOpenCloseTime != null ? weeklyOpenCloseTime.getSunday() : null);
                break;
        }

        String coverImageUrl = null;
        if (restaurant.getCoverImage() != null) {
            coverImageUrl = ImagePathParser.parse(restaurant.getCoverImage().getUuidFileName());
        }

        RestaurantCardDTO restaurantCardDTO = new RestaurantCardDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory().getTitle(),
                distanceString,
                openingHour,
                coverImageUrl
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

    public List<RestaurantSearchResultDTO> getRestaurantsBySearchText(String searchText) {
        return restaurantRepository.searchByNameAndMenu(searchText);
    }

    public Restaurant getRestaurantByOwnerId(Long id) {
        return restaurantRepository.findByOwnerId(id);
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    /***
     * 새로운 메뉴 추가
     * @param menu
     */
    public void addNewMenu(Menu menu) {
        menuRepository.save(menu);
    }

    /***
     * 기존 메뉴 수정
     * @param m
     */
    public void updateMenu(Menu m) {
        Menu menu = entityManager.find(Menu.class, m.getId());
        menu.setName(m.getName());
        menu.setPrice(m.getPrice());
    }

    @Transactional
    public void modifyOperatingHour(Long restaurantId, Map<String, OperatingHourDTO> operatingHours) {
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setMonday(operatingHours.get("월").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("월").getOpen(), operatingHours.get("월").getClose(), true) : null);
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setTuesday(operatingHours.get("화").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("화").getOpen(), operatingHours.get("화").getClose(), true) : null);
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setWednesday(operatingHours.get("수").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("수").getOpen(), operatingHours.get("수").getClose(), true) : null);
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setThursday(operatingHours.get("목").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("목").getOpen(), operatingHours.get("목").getClose(), true) : null);
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setFriday(operatingHours.get("금").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("금").getOpen(), operatingHours.get("금").getClose(), true) : null);
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setSaturday(operatingHours.get("토").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("토").getOpen(), operatingHours.get("토").getClose(), true) : null);
        entityManager.find(Restaurant.class, restaurantId).getWeeklyOpenCloseTime().setSunday(operatingHours.get("일").getIsOpen() ? new OpenCloseTime(null, operatingHours.get("일").getOpen(), operatingHours.get("일").getClose(), true) : null);
    }
}
