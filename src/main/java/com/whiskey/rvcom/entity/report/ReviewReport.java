package com.whiskey.rvcom.entity.report;

import com.whiskey.rvcom.entity.review.Review;
import jakarta.persistence.*;

@Table(name = "tbl_review_report")
@Entity
public class ReviewReport extends Report {
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}