package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.RestaurantNotice;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantNoticeRepository extends JpaRepository<RestaurantNotice, Long> {
     @Query("""
             SELECT n FROM RestaurantNotice n
             WHERE n.restaurant.id = :restaurantId and
                n.isDeleted = false
             """)
     List<RestaurantNotice> findByRestaurantId(Long restaurantId);

     @Query("""
             select new com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO(n.id, n.title, n.content, n.isDeleted, n.createdAt)
             from RestaurantNotice n
             where n.restaurant.id = :restaurantId
             and   n.isDeleted = false
             order by n.createdAt desc
             """)
     Page<RestaurantNoticeResponseDTO> getPagedRestaurantNotices(@Param("restaurantId") Long restaurantId, Pageable pageable);
}
