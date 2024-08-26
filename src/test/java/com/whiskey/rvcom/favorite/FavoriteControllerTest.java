package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FavoriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private FavoriteController favoriteController;

    private MockHttpSession session;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();

        testMember = new Member();
        testMember.setId(1L);
        testMember.setName("테스트 사용자");

        session = new MockHttpSession();
        session.setAttribute("member", testMember);
    }

    @Test
    @DisplayName("회원의 즐겨찾기 목록 조회 테스트")
    public void getFavoritesByMember_shouldReturnListOfFavorites() throws Exception {
        // 가짜 데이터 준비
        when(favoriteService.getFavoritesByMember(testMember)).thenReturn(List.of());

        // 요청 실행 및 검증
        mockMvc.perform(get("/api/favorites/member")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("즐겨찾기 추가 테스트")
    public void addFavorite_shouldSaveNewFavorite() throws Exception {
        // 가짜 데이터 준비
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Favorite favorite = new Favorite();
        favorite.setMember(testMember);
        favorite.setRestaurant(restaurant);

        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);
        when(favoriteService.addFavorite(testMember, restaurant)).thenReturn(favorite);

        // 요청 실행 및 검증
        mockMvc.perform(post("/api/favorites")
                        .session(session)
                        .param("restaurantId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    public void removeFavorite_shouldDeleteFavorite() throws Exception {
        // 가짜 데이터 준비
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);
        doNothing().when(favoriteService).removeFavorite(any(Member.class), any(Restaurant.class));

        // 요청 실행 및 검증
        mockMvc.perform(post("/api/favorites/remove")
                        .session(session)
                        .param("restaurantId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("즐겨찾기 존재 여부 테스트")
    public void isFavorite_shouldReturnTrueWhenFavoriteExists() throws Exception {
        // 가짜 데이터 준비
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);
        when(favoriteService.isFavorite(testMember, restaurant)).thenReturn(true);

        // 요청 실행 및 검증
        mockMvc.perform(get("/api/favorites/check")
                        .session(session)
                        .param("restaurantId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("즐겨찾기 개수 조회 테스트")
    public void getFavoriteCount_shouldReturnCorrectCount() throws Exception {
        // 가짜 데이터 준비
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);
        when(favoriteService.getFavoriteCount(restaurant)).thenReturn(2L);

        // 요청 실행 및 검증
        mockMvc.perform(get("/api/favorites/count/1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
