package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    // 회원의 즐겨찾기 목록 조회
    public List<Favorite> getFavoritesByMember(Member member) {
        return favoriteRepository.findByMember(member);
    }

    // 즐겨찾기 추가
    @Transactional
    public Favorite addFavorite(Member member, Restaurant restaurant) {
        // TODO: 중복 즐겨찾기 체크 로직 추가 필요
        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setRestaurant(restaurant);
        return favoriteRepository.save(favorite);
    }

    // 즐겨찾기 삭제
    @Transactional
    public void removeFavorite(Member member, Restaurant restaurant) {
        favoriteRepository.deleteByMemberAndRestaurant(member, restaurant);
    }

    // 즐겨찾기 업데이트 (필요한 경우)
    @Transactional
    public Favorite updateFavorite(Favorite favorite) {
        // TODO: 업데이트 전 존재 여부 확인 로직 추가 필요
        return favoriteRepository.save(favorite);
    }
}
