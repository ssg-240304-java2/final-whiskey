package com.whiskey.rvcom.restaurant.controller;

import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.resource.ImageFile;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@Slf4j
public class RestaurantMenuController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final ImageFileService imageFileService;

    public RestaurantMenuController(RestaurantService restaurantService, MenuRepository menuRepository, MenuService menuService, ImageFileService imageFileService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.imageFileService = imageFileService;
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


        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>들어왔니?");

        Restaurant restaurant = restaurantService.getRestaurantById(Long.parseLong(data.get("restaurantId").toString()));

        log.info(data.get("image").toString());
        ImageFile imageFile = null;
        if (data.get("image") != null) {
            imageFile = imageFileService.getImageFile(Long.parseLong(data.get("image").toString()));
        }


        if (data.get("id") == null || data.get("id").equals("")) {
            // 새로운 메뉴 추가
            log.info(">>>> 새로운 메뉴 추가");
            Menu menu = new Menu(null, (String) data.get("name"), Integer.parseInt((String) data.get("price")), imageFile, restaurant);
            log.info("menu : " + menu.getImage().getUuidFileName());
            restaurantService.addNewMenu(menu);
        } else {
            // 기존 메뉴 수정
            log.info(">>>> 기존 메뉴 수정");
            Menu menu = new Menu(Long.parseLong(data.get("id").toString()), (String) data.get("name"), Integer.parseInt((String) data.get("price")), imageFile, restaurant);
            log.info("menu : " + menu.getImage().getUuidFileName());
            restaurantService.updateMenu(menu);
        }
        return "메뉴 저장";
    }

    @PostMapping("/menu/image/upload")
    @ResponseBody
    public ResponseEntity<Long> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
            return ResponseEntity.badRequest().build();
        }
        try {
            // 이미지 파일 업로드 및 UUID 파일명 반환
            Long uuidFileName = imageFileService.uploadFile(file).getId();
            return ResponseEntity.ok(uuidFileName);
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
