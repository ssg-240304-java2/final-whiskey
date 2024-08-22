package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.entity.review.ReviewImage;
import com.whiskey.rvcom.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * 회원의 즐겨찾기 목록을 조회
     * 이 기능은 사용자 프로필 페이지나 즐겨찾기 관리 페이지에서 사용될 예정.
     * 
     * @param member 조회할 회원 객체
     * @return 해당 회원의 즐겨찾기 목록
     */
    public List<Favorite> getFavoritesByMember(Member member) {
        return favoriteRepository.findByMember(member); 
    }


    /**
     * 새로운 즐겨찾기를 추가.
     * 이 메서드는 사용자가 음식점 상세 페이지에서 '즐겨찾기 추가' 버튼을 클릭했을 때 호출될 수 있다.
     * 중복 즐겨찾기를 방지하기 위해 이미 존재하는 즐겨찾기인지 확인.
     * 
     * @param member 즐겨찾기를 추가하는 회원
     * @param restaurant 즐겨찾기에 추가할 음식점
     * @return 새로 생성된 즐겨찾기 객체
     * @throws IllegalStateException 이미 즐겨찾기에 추가된 음식점일 경우 발생
     */
    @Transactional
    public Favorite addFavorite(Member member, Restaurant restaurant) {
        // 중복 즐겨찾기 체크
        Optional<Favorite> existingFavorite = favoriteRepository.findByMemberAndRestaurant(member, restaurant);
        if (existingFavorite.isPresent()) {
            throw new IllegalStateException("이미 즐겨찾기에 추가된 음식점입니다.");
        }

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setRestaurant(restaurant);
        return favoriteRepository.save(favorite);
    }

    /**
     * 즐겨찾기를 삭제.
     * 이 메서드는 사용자가 즐겨찾기 목록이나 음식점 상세 페이지에서 '즐겨찾기 삭제' 버튼을 클릭했을 때 호출될 수 있음.
     * 
     * @param member 즐겨찾기를 삭제하는 회원
     * @param restaurant 즐겨찾기에서 삭제할 음식점
     */
    @Transactional
    public void removeFavorite(Member member, Restaurant restaurant) {
        favoriteRepository.deleteByMemberAndRestaurant(member, restaurant);
    }

    /**
     * 특정 음식점이 회원의 즐겨찾기에 포함되어 있는지 확인.
     * 이 메서드는 음식점 목록이나 상세 페이지에서 즐겨찾기 상태를 표시할 때 사용될 수 있슴다. (아직 모르겠)
     * 
     * @param member 확인할 회원
     * @param restaurant 확인할 음식점
     * @return 즐겨찾기에 포함되어 있으면 true, 아니면 false
     */
    public boolean isFavorite(Member member, Restaurant restaurant) {
        return favoriteRepository.findByMemberAndRestaurant(member, restaurant).isPresent();
    }

    /**
     * 특정 음식점의 즐겨찾기 수를 조회합니다.
     * 이 메서드는 음식점의 인기도를 나타내는 지표로 사용될 수 있으며, 음식점 상세 페이지나 목록에서 표시될 수 있다.
     * 
     * @param restaurant 즐겨찾기 수를 조회할 음식점
     * @return 해당 음식점의 즐겨찾기 수
     */
    public long getFavoriteCount(Restaurant restaurant) {
        return favoriteRepository.countByRestaurant(restaurant);
    }

    // TODO: 회원 정보 변경 시 즐겨찾기 정보 업데이트
    // 회원 정보가 변경될 때 관련된 즐겨찾기 정보도 함께 업데이트해야 합니다.
    // 예를 들어, 회원의 이름이 변경되면 즐겨찾기 객체 내의 회원 정보도 갱신되어야 합니다.

    // TODO: 음식점 정보 변경 시 즐겨찾기 정보 업데이트
    // 음식점 정보가 변경될 때 관련된 즐겨찾기 정보도 함께 업데이트해야 합니다.
    // 예를 들어, 음식점의 이름이 변경되면 즐겨찾기 객체 내의 음식점 정보도 갱신되어야 합니다.

    // TODO: 즐겨찾기 목록 페이징 처리
    // 사용자의 즐겨찾기 목록이 많을 경우, 페이지네이션을 구현하여 성능을 개선 및 사용자 경험을 향상시켜야 함.
    // Spring Data JPA의 Pageable 인터페이스를 활용하여 구현할 수 있다네요...

    // TODO: 즐겨찾기 목록 정렬 기능 (최신순, 이름순 등)
    // 사용자가 자신의 즐겨찾기 목록을 다양한 기준으로 정렬할 수 있는 기능이 필요하다면?
    // 최신 추가순, 음식점 이름순, 방문 횟수순 등 다양한 정렬 옵션을 제공할 수 있습니다.
}
