package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.favorite.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite , Long> {

}
