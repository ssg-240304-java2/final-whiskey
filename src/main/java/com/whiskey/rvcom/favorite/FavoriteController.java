package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.Member.service.MemberManagementService;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.ImageFileRepository;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 즐겨찾기 관련 API를 처리하는 컨트롤러
 * 회원의 즐겨찾기 목록 조회, 추가, 삭제 및 음식점의 즐겨찾기 수 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final RestaurantService restaurantService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, RestaurantService restaurantService) {
        this.favoriteService = favoriteService;
        this.restaurantService = restaurantService;
    }

    /**
     * 회원의 즐겨찾기 목록을 조회합니다.
     * @param session 현재 로그인한 세션
     * @return 즐겨찾기 목록
     */
    @GetMapping("/member")
    public ResponseEntity<List<Map<String, Object>>> getFavoritesByMember(HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        List<Favorite> favorites = favoriteService.getFavoritesByMember(member);
        List<Map<String, Object>> favoritesWithImages = favorites.stream().map(favorite -> {
            Map<String, Object> favoriteMap = new HashMap<>();
            favoriteMap.put("favorite", favorite);

            // 커버 이미지 URL 생성
            ImageFile coverImage = favorite.getRestaurant().getCoverImage();
            if (coverImage != null) {
                favoriteMap.put("imageUrl", ImagePathParser.parse(coverImage.getUuidFileName()));
            } else {
                favoriteMap.put("imageUrl", "https://i.kym-cdn.com/entries/icons/facebook/000/049/273/cover11.jpg"); // 기본 이미지 설정
            }

            return favoriteMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(favoritesWithImages);
    }

    /**
     * 새로운 즐겨찾기를 추가합니다.
     * @param session 현재 로그인한 세션
     * @param restaurantId 음식점 ID
     * @return 추가된 즐겨찾기 정보 또는 에러 메시지
     */
    @PostMapping
    public ResponseEntity<?> addFavorite(HttpSession session, @RequestParam Long restaurantId) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(restaurantId);
            Favorite favorite = favoriteService.addFavorite(member, restaurant);
            return ResponseEntity.ok(favorite);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 즐겨찾기를 삭제합니다.
     * @param session 현재 로그인한 세션
     * @param restaurantId 음식점 ID
     * @return 삭제 성공 여부
     */
    @PostMapping("/remove")
    @Transactional
    public ResponseEntity<String> removeFavorite(@RequestParam("restaurantId") Long restaurantId, HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        favoriteService.removeFavorite(member, restaurant);

        return ResponseEntity.ok("Favorite removed");
    }

    /**
     * 특정 회원이 특정 음식점을 즐겨찾기했는지 확인합니다.
     * @param session 현재 로그인한 세션
     * @param restaurantId 음식점 ID
     * @return 즐겨찾기 여부
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(HttpSession session, @RequestParam Long restaurantId) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        boolean isFavorite = favoriteService.isFavorite(member, restaurant);
        return ResponseEntity.ok(isFavorite);
    }

    /**
     * 특정 음식점의 즐겨찾기 수를 조회합니다.
     * @param restaurantId 음식점 ID
     * @return 즐겨찾기 수
     */
    @GetMapping("/count/{restaurantId}")
    public ResponseEntity<Long> getFavoriteCount(@PathVariable Long restaurantId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        long count = favoriteService.getFavoriteCount(restaurant);
        return ResponseEntity.ok(count);
    }

    // TODO: 즐겨찾기 목록 페이징 처리 API 추가
    // TODO: 즐겨찾기 목록 정렬 기능 API 추가
}


