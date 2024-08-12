package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    private Member testMember;
    private Restaurant testRestaurant;
    private Favorite testFavorite;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setId(1L);

        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);

        testFavorite = new Favorite();
        testFavorite.setId(1L);
        testFavorite.setMember(testMember);
        testFavorite.setRestaurant(testRestaurant);
    }

    @Test
    void getFavoritesByMember() throws Exception {
        List<Favorite> favorites = Arrays.asList(testFavorite);
        when(favoriteService.getFavoritesByMember(any(Member.class))).thenReturn(favorites);

        mockMvc.perform(get("/api/favorites/member/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void addFavorite() throws Exception {
        when(favoriteService.addFavorite(any(Member.class), any(Restaurant.class))).thenReturn(testFavorite);

        mockMvc.perform(post("/api/favorites")
                .param("memberId", "1")
                .param("restaurantId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void removeFavorite() throws Exception {
        mockMvc.perform(delete("/api/favorites")
                .param("memberId", "1")
                .param("restaurantId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateFavorite() throws Exception {
        when(favoriteService.updateFavorite(any(Favorite.class))).thenReturn(testFavorite);

        mockMvc.perform(put("/api/favorites/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"member\":{\"id\":1},\"restaurant\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }
}
