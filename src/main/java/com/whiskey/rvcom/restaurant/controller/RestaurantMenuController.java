package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.repository.MenuRepository;
import com.whiskey.rvcom.restaurant.service.MenuService;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
public class RestaurantMenuController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;

    public RestaurantMenuController(RestaurantService restaurantService, MenuRepository menuRepository, MenuService menuService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
    }

    @GetMapping("/api/restaurant/menu/{id}")
    @ResponseBody
    public Menu getMenuList(@PathVariable Long id) {
        return menuService.getMenuData(id);
    }

    @DeleteMapping("/api/restaurant/deleteMenu/{id}")
    @Transactional
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
        try {
            menuService.deleteMenuById(id);
            return ResponseEntity.ok("메뉴 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제 실패");
        }
    }

    @PostMapping("/api/restaurant/saveMenu")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @ResponseBody
    public String addMenu(@RequestParam Map<Object, Object> data) {

        Restaurant restaurant = restaurantService.getRestaurantById(Long.parseLong(data.get("restaurantId").toString()));

        if (data.get("id") == null || data.get("id").equals("")) {
            // 새로운 메뉴 추가
            Menu menu = new Menu(null, (String) data.get("name"), Integer.parseInt((String) data.get("price")), null, restaurant);
            restaurantService.addNewMenu(menu);
        } else {
            // 기존 메뉴 수정
            Menu menu = new Menu(Long.parseLong(data.get("id").toString()), (String) data.get("name"), Integer.parseInt((String) data.get("price")), null, restaurant);
            restaurantService.updateMenu(menu);
        }
        return "메뉴 저장";
    }
}
