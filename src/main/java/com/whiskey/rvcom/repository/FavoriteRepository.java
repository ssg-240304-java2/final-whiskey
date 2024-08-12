package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 특정 회원의 즐겨찾기 목록 조회
    List<Favorite> findByMemberId(Long memberId);

    // 특정 회원의 특정 음식점 즐겨찾기 여부 확인
    boolean existsByMemberIdAndRestaurantId(Long memberId, Long restaurantId);

    // 특정 회원의 특정 음식점 즐겨찾기 삭제
    void deleteByMemberIdAndRestaurantId(Long memberId, Long restaurantId);
}
