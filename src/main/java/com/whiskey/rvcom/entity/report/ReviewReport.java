package com.whiskey.rvcom.entity.report;

import com.whiskey.rvcom.entity.review.Review;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Table(name = "tbl_review_report")
@Entity
@Getter
@Setter
public class ReviewReport extends Report {
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}