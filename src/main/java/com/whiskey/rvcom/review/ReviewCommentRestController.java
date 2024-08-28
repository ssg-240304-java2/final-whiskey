package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.review.ReviewComment;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.review.dto.ReviewCommentAddRequestModel;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewCommentRestController {
    private final ReviewCommentService reviewCommentService;
    private final MemberRepository memberRepository;
    private final ReviewService reviewService;

    @PostMapping("/review/comment/add")
    public ResponseEntity<List<ReviewComment>> addComment(@RequestBody ReviewCommentAddRequestModel reviewComment, HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if(member == null) {
            return ResponseEntity.internalServerError().build();
            // todo. throw exceiption or return error message as async response
        }
        System.out.println("들어온 요청: " + reviewComment);

        // 리뷰 댓글 추가
        ReviewComment comment = new ReviewComment();
        comment.setCommenter(member);
        comment.setReview(reviewService.getReviewById(reviewComment.getReviewId()));
        comment.setContent(reviewComment.getContent());

        reviewCommentService.saveComment(comment);

        List<ReviewComment> commentsForReview = reviewCommentService.getCommentsForReview(comment.getReview());

        return ResponseEntity.ok(commentsForReview);
    }
}