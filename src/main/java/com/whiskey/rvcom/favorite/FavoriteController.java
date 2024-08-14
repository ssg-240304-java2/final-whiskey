package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 즐겨찾기 관련 API를 처리하는 컨트롤러
 * 회원의 즐겨찾기 목록 조회, 추가, 삭제 및 음식점의 즐겨찾기 수 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 생성자를 통한 의존성 주입
     * @param favoriteService 즐겨찾기 서비스
     */
    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * 회원의 즐겨찾기 목록을 조회합니다.
     * @param memberId 회원 ID
     * @return 즐겨찾기 목록
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Favorite>> getFavoritesByMember(@PathVariable Long memberId) {
        // TODO: MemberService 구현 후 아래 주석 해제 및 임시 코드 제거
        // Member member = memberService.getMemberById(memberId);
        Member member = new Member(); // 임시 코드
        member.setId(memberId); 
        return ResponseEntity.ok(favoriteService.getFavoritesByMember(member));
    }

    /**
     * 새로운 즐겨찾기를 추가합니다.
     * @param memberId 회원 ID
     * @param restaurantId 음식점 ID
     * @return 추가된 즐겨찾기 정보 또는 에러 메시지
     */
    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestParam Long memberId, @RequestParam Long restaurantId) {
        try {
            // TODO: MemberService와 RestaurantService 구현 후 아래 주석 해제 및 임시 코드 제거
            // Member member = memberService.getMemberById(memberId);
            // Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
            Member member = new Member(); // 임시 코드
            member.setId(memberId);
            Restaurant restaurant = new Restaurant(); // 임시 코드
            restaurant.setId(restaurantId);
            Favorite favorite = favoriteService.addFavorite(member, restaurant);
            return ResponseEntity.ok(favorite); 
        } catch (IllegalStateException e) { // 중복 즐겨찾기 예외 처리
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 즐겨찾기를 삭제합니다.
     * @param memberId 회원 ID
     * @param restaurantId 음식점 ID
     * @return 삭제 성공 여부
     */
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

    /**
     * 특정 회원이 특정 음식점을 즐겨찾기했는지 확인합니다.
     * @param memberId 회원 ID
     * @param restaurantId 음식점 ID
     * @return 즐겨찾기 여부
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(@RequestParam Long memberId, @RequestParam Long restaurantId) {
        // TODO: MemberService와 RestaurantService 구현 후 아래 주석 해제 및 임시 코드 제거
        // Member member = memberService.getMemberById(memberId);
        // Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        Member member = new Member(); // 임시 코드
        member.setId(memberId);
        Restaurant restaurant = new Restaurant(); // 임시 코드
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
        // TODO: RestaurantService 구현 후 아래 주석 해제 및 임시 코드 제거
        // Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        Restaurant restaurant = new Restaurant(); // 임시 코드
        restaurant.setId(restaurantId);
        long count = favoriteService.getFavoriteCount(restaurant);
        return ResponseEntity.ok(count);
    }

    // TODO: 즐겨찾기 목록 페이징 처리 API 추가
    // TODO: 즐겨찾기 목록 정렬 기능 API 추가
}
