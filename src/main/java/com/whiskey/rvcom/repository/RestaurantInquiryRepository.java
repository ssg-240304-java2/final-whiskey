package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantInquiryRepository extends JpaRepository<RestaurantInquiry, Long> {

    @Query("""
            select i
            from RestaurantInquiry i
            where i.restaurant.id = :restaurantId
            and i.reply.deletedAt is null
            """)
    List<RestaurantInquiry> findAllByRestaurantId(Long restaurantId);

    Optional<RestaurantInquiry> findByReplyId(Long replyId);

    Optional<RestaurantInquiry> findByContent(String content);
}
