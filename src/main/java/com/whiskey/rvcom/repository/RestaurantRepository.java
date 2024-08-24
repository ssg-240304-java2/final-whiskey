package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.restaurant.dto.RestaurantSearchResultDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r order by ( 6371 * acos( cos( radians(:lat) ) * cos( radians( r.address.latitude ) ) * cos( radians( r.address.longitude ) - radians(:lng) ) + sin( radians(:lat) ) * sin( radians( r.address.latitude ) ) ) ) ASC")
    List<Restaurant> findAllByDistance(double lat, double lng);

    // Restaurant db에서 검색어를 포함하는 식당 정보를 가져오는 jpa
    @Query("select new com.whiskey.rvcom.restaurant.dto.RestaurantSearchResultDTO(r.id, r.name, r.category, r.address) from Restaurant r where r.name like %:searchText%")
    List<RestaurantSearchResultDTO> findAllByNameContaining(String searchText);
}
