package com.whiskey.rvcom.favorite;

import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);

        restaurant = new Restaurant();
        restaurant.setId(1L);
    }

    @Test
    void getFavoritesByMember_shouldReturnListOfFavorites() {
        // Given
        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setRestaurant(restaurant);
        List<Favorite> expectedFavorites = Arrays.asList(favorite);

        when(favoriteRepository.findByMember(member)).thenReturn(expectedFavorites);

        // When
        List<Favorite> actualFavorites = favoriteService.getFavoritesByMember(member);

        // Then
        assertEquals(expectedFavorites, actualFavorites);
        verify(favoriteRepository).findByMember(member);
    }

    @Test
    void addFavorite_shouldSaveNewFavorite() {
        // Given
        when(favoriteRepository.findByMemberAndRestaurant(member, restaurant)).thenReturn(Optional.empty());
        when(favoriteRepository.save(any(Favorite.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Favorite savedFavorite = favoriteService.addFavorite(member, restaurant);

        // Then
        assertNotNull(savedFavorite);
        assertEquals(member, savedFavorite.getMember());
        assertEquals(restaurant, savedFavorite.getRestaurant());
        verify(favoriteRepository).findByMemberAndRestaurant(member, restaurant);
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_shouldThrowExceptionWhenAlreadyExists() {
        // Given
        when(favoriteRepository.findByMemberAndRestaurant(member, restaurant)).thenReturn(Optional.of(new Favorite()));

        // When & Then
        assertThrows(IllegalStateException.class, () -> favoriteService.addFavorite(member, restaurant));
        verify(favoriteRepository).findByMemberAndRestaurant(member, restaurant);
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    void removeFavorite_shouldCallRepositoryMethod() {
        // When
        favoriteService.removeFavorite(member, restaurant);

        // Then
        verify(favoriteRepository).deleteByMemberAndRestaurant(member, restaurant);
    }

    @Test
    void isFavorite_shouldReturnTrueWhenFavoriteExists() {
        // Given
        when(favoriteRepository.findByMemberAndRestaurant(member, restaurant)).thenReturn(Optional.of(new Favorite()));

        // When
        boolean result = favoriteService.isFavorite(member, restaurant);

        // Then
        assertTrue(result);
        verify(favoriteRepository).findByMemberAndRestaurant(member, restaurant);
    }

    @Test
    void isFavorite_shouldReturnFalseWhenFavoriteDoesNotExist() {
        // Given
        when(favoriteRepository.findByMemberAndRestaurant(member, restaurant)).thenReturn(Optional.empty());

        // When
        boolean result = favoriteService.isFavorite(member, restaurant);

        // Then
        assertFalse(result);
        verify(favoriteRepository).findByMemberAndRestaurant(member, restaurant);
    }

    @Test
    void getFavoriteCount_shouldReturnCorrectCount() {
        // Given
        long expectedCount = 5L;
        when(favoriteRepository.countByRestaurant(restaurant)).thenReturn(expectedCount);

        // When
        long actualCount = favoriteService.getFavoriteCount(restaurant);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(favoriteRepository).countByRestaurant(restaurant);
    }
}
