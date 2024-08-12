package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // 회원의 즐겨찾기 목록 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Favorite>> getFavoritesByMember(@PathVariable Long memberId) {
        // TODO: MemberService 구현 후 아래 주석 해제 및 임시 코드 제거
        // Member member = memberService.getMemberById(memberId);
        Member member = new Member(); // 임시 코드
        member.setId(memberId);
        return ResponseEntity.ok(favoriteService.getFavoritesByMember(member));
    }

    // 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<Favorite> addFavorite(@RequestParam Long memberId, @RequestParam Long restaurantId) {
        // TODO: MemberService와 RestaurantService 구현 후 아래 주석 해제 및 임시 코드 제거
        // Member member = memberService.getMemberById(memberId);
        // Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        Member member = new Member(); // 임시 코드
        member.setId(memberId);
        Restaurant restaurant = new Restaurant(); // 임시 코드
        restaurant.setId(restaurantId);
        return ResponseEntity.ok(favoriteService.addFavorite(member, restaurant));
    }

    // 즐겨찾기 삭제
    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long memberId, @RequestParam Long restaurantId) {
        // TODO: MemberService와 RestaurantService 구현 후 아래 주석 해제 및 임시 코드 제거
        // Member member = memberService.getMemberById(memberId);
        // Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        Member member = new Member(); // 임시 코드
        member.setId(memberId);
        Restaurant restaurant = new Restaurant(); // 임시 코드
        restaurant.setId(restaurantId);
        favoriteService.removeFavorite(member, restaurant);
        return ResponseEntity.ok().build();
    }

    // 즐겨찾기 업데이트 (필요한 경우)
    @PutMapping("/{favoriteId}")
    public ResponseEntity<Favorite> updateFavorite(@PathVariable Long favoriteId, @RequestBody Favorite favorite) {
        // TODO: 업데이트 전 favoriteId에 해당하는 즐겨찾기 존재 여부 확인 로직 추가 필요
        favorite.setId(favoriteId);
        return ResponseEntity.ok(favoriteService.updateFavorite(favorite));
    }
}
