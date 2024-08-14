package com.whiskey.rvcom.entity.report;

import com.whiskey.rvcom.entity.review.ReviewComment;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tbl_review_comment_report")
@Entity
@Getter
@Setter
public class ReviewCommentReport extends Report {
    @ManyToOne
    @JoinColumn(name = "review_comment_id", nullable = false)
    private ReviewComment reviewComment;
}