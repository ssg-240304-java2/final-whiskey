package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 특정 회원의 즐겨찾기 목록 조회
    List<Favorite> findByMember(Member member);

    // 특정 회원의 특정 음식점 즐겨찾기 삭제 
    void deleteByMemberAndRestaurant(Member member, Restaurant restaurant);
}
