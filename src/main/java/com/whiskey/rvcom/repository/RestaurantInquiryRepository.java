package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.inquiry.restaurant.RestaurantInquiry;
import com.whiskey.rvcom.inquiry.dto.RestaurantInquiryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantInquiryRepository extends JpaRepository<RestaurantInquiry, Long> {

    @Query("""
            select i
            from RestaurantInquiry i
            left join i.reply r
            where i.restaurant.id = :restaurantId
            and i.deletedAt is null
            and (r is null or r.deletedAt is null)
            """)
    List<RestaurantInquiry> findAllByRestaurantId(Long restaurantId);

    @Query("""
            select i
            from RestaurantInquiry i
            left join RestaurantInquiryReply r on i.reply.id = r.id
            where i.restaurant.id = :restaurantId
            and i.deletedAt is null
            and (r is null or r.deletedAt is null)
            """)
    Page<RestaurantInquiry> getPagedRestaurantInquiries(@Param("restaurantId") Long restaurantId, Pageable pageable);

    Optional<RestaurantInquiry> findByReplyId(Long replyId);

    Optional<RestaurantInquiry> findByContent(String content);

    @Query("""
            select count(i)
            from RestaurantInquiry i
            where i.restaurant.id = :restaurantId
            and i.reply is null
            and i.deletedAt is null
            """)
    int countUnansweredInquiries(Long restaurantId);
}
