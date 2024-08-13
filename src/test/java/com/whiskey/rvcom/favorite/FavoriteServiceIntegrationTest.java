package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.repository.AddressRepository;
import com.whiskey.rvcom.repository.FavoriteRepository;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FavoriteServiceIntegrationTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;
    

    private Member testMember;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        // 테스트용 회원 생성
        testMember = new Member();
        testMember.setName("Test User");
        testMember.setNickname("testuser");
        testMember.setLoginId("testLoginId");
        testMember.setEmail("test@example.com");
        testMember.setPassword("testPassword");
        testMember.setCreatedAt(java.time.LocalDateTime.now());
        testMember.setActive(true);
        testMember.setRole(Role.USER);
        testMember.setLoginType(LoginType.BASIC);
        testMember = memberRepository.save(testMember);
    
        // 테스트용 주소 생성
        Address address = new Address();
        address.setName("Test Address");
        address.setLatitude(37.5665);
        address.setLongitude(126.9780);
        address = addressRepository.save(address);

        // 테스트용 식당 생성
        testRestaurant = new Restaurant();
        testRestaurant.setName("Test Restaurant");
        testRestaurant.setCategory(RestaurantCategory.KOREAN);
        testRestaurant.setNumber("123-456-7890");
        testRestaurant.setVisible(true);
        testRestaurant.setOwner(testMember);
        testRestaurant.setAddress(address);
        
        testRestaurant = restaurantRepository.save(testRestaurant);
    }
    
    @Test
    @DisplayName("회원의 즐겨찾기 목록 조회 테스트")
    void getFavoritesByMember_shouldReturnListOfFavorites() {
        // Given: 테스트 회원의 즐겨찾기 생성
        Favorite favorite = new Favorite();
        favorite.setMember(testMember);
        favorite.setRestaurant(testRestaurant);
        favoriteRepository.save(favorite);

        // When: 회원의 즐겨찾기 목록 조회
        // favoriteService를 사용하여 테스트 회원의 즐겨찾기 목록을 가져옵니다.
        List<Favorite> favorites = favoriteService.getFavoritesByMember(testMember);

        // Then: 조회된 즐겨찾기 목록 검증
        // 1. 즐겨찾기 목록이 비어있지 않은지 확인합니다.
        assertFalse(favorites.isEmpty());
        // 2. 즐겨찾기 목록의 크기가 1인지 확인합니다 (우리가 추가한 즐겨찾기 하나만 있어야 함).
        assertEquals(1, favorites.size());
        // 3. 첫 번째 즐겨찾기의 회원이 테스트 회원과 일치하는지 확인합니다.
        assertEquals(testMember, favorites.get(0).getMember());
        // 4. 첫 번째 즐겨찾기의 식당이 테스트 식당과 일치하는지 확인합니다.
        assertEquals(testRestaurant, favorites.get(0).getRestaurant());
    }

    @Test
    @DisplayName("즐겨찾기 추가 테스트")
    void addFavorite_shouldSaveNewFavorite() {
        // When: 새로운 즐겨찾기 추가
        // favoriteService를 사용하여 테스트 회원과 테스트 식당으로 새로운 즐겨찾기를 추가합니다.
        Favorite savedFavorite = favoriteService.addFavorite(testMember, testRestaurant);

        // Then: 저장된 즐겨찾기 검증
        // 1. 저장된 즐겨찾기 객체가 null이 아닌지 확인합니다.
        assertNotNull(savedFavorite);
        // 2. 저장된 즐겨찾기의 ID가 null이 아닌지 확인합니다 (데이터베이스에 저장되었다는 의미).
        assertNotNull(savedFavorite.getId());
        // 3. 저장된 즐겨찾기의 회원이 테스트 회원과 일치하는지 확인합니다.
        assertEquals(testMember, savedFavorite.getMember());
        // 4. 저장된 즐겨찾기의 식당이 테스트 식당과 일치하는지 확인합니다.
        assertEquals(testRestaurant, savedFavorite.getRestaurant());
    }

    @Test
    @DisplayName("이미 즐겨찾기에 추가된 식당인 경우 예외 발생 테스트")
    void addFavorite_shouldThrowExceptionWhenAlreadyExists() {
        // Given: 이미 즐겨찾기에 추가된 상태
        favoriteService.addFavorite(testMember, testRestaurant);

        // When & Then: 동일한 즐겨찾기 추가 시 예외 발생 확인
        assertThrows(IllegalStateException.class, () -> favoriteService.addFavorite(testMember, testRestaurant));
    }

    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void removeFavorite_shouldDeleteFavorite() {
        // Given: 즐겨찾기 추가
        favoriteService.addFavorite(testMember, testRestaurant);

        // When: 즐겨찾기 삭제
        favoriteService.removeFavorite(testMember, testRestaurant);

        // Then: 삭제된 즐겨찾기 확인
        assertFalse(favoriteRepository.findByMemberAndRestaurant(testMember, testRestaurant).isPresent());
    }

    @Test
    @DisplayName("즐겨찾기 존재 여부 테스트")
    void isFavorite_shouldReturnTrueWhenFavoriteExists() {
        // Given: 즐겨찾기 추가
        favoriteService.addFavorite(testMember, testRestaurant);

        // When: 즐겨찾기 존재 여부 확인
        boolean result = favoriteService.isFavorite(testMember, testRestaurant);

        // Then: 즐겨찾기가 존재하는지 검증
        assertTrue(result);
    }

    @Test
    @DisplayName("즐겨찾기 존재 여부 테스트 (존재하지 않는 경우)")
    void isFavorite_shouldReturnFalseWhenFavoriteDoesNotExist() {
        // When: 즐겨찾기 존재 여부 확인 (추가하지 않은 상태)
        boolean result = favoriteService.isFavorite(testMember, testRestaurant);

        // Then: 즐겨찾기가 존재하지 않음을 검증
        assertFalse(result);
    }

    @Test
    @DisplayName("즐겨찾기 개수 조회 테스트")
    void getFavoriteCount_shouldReturnCorrectCount() {
        // Given: 테스트 준비
        // 첫 번째 사용자(testMember)가 식당을 즐겨찾기에 추가
        favoriteService.addFavorite(testMember, testRestaurant);
        
        // 두 번째 사용자 생성 및 저장
        Member anotherMember = new Member();
        anotherMember.setName("다른 사용자");
        anotherMember.setNickname("다른사용자");
        anotherMember.setLoginId("anotherLoginId");
        anotherMember.setEmail("another@example.com");
        anotherMember.setPassword("anotherPassword");
        anotherMember.setCreatedAt(java.time.LocalDateTime.now());
        anotherMember.setActive(true);
        anotherMember.setRole(Role.USER);
        anotherMember.setLoginType(LoginType.BASIC);
        anotherMember = memberRepository.save(anotherMember);
        
        // 두 번째 사용자(anotherMember)도 같은 식당을 즐겨찾기에 추가
        favoriteService.addFavorite(anotherMember, testRestaurant);
    
        // When: 테스트 실행
        // 해당 식당의 즐겨찾기 개수 조회
        long count = favoriteService.getFavoriteCount(testRestaurant);
    
        // Then: 결과 검증
        // 두 명의 사용자가 즐겨찾기에 추가했으므로 개수는 2여야 함
        assertEquals(2, count);
    }
    

    // TODO: 다양한 예외 상황에 대한 테스트 케이스 추가
    // TODO: 대량의 데이터를 사용한 성능 테스트 추가
}
