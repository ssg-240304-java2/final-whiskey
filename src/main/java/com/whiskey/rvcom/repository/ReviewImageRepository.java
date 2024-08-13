package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.review.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview(Review review);
}
