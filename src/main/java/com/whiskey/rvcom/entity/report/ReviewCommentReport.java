package com.whiskey.rvcom.entity.report;

import com.whiskey.rvcom.entity.review.ReviewComment;
import jakarta.persistence.*;

@Table(name = "tbl_review_comment_report")
@Entity
public class ReviewCommentReport extends Report {
    @ManyToOne
    @JoinColumn(name = "review_comment_id")
    private ReviewComment reviewComment;
}