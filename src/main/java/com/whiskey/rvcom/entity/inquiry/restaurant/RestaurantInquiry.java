package com.whiskey.rvcom.entity.inquiry.restaurant;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_restaurant_inquiry")
@Entity
@Getter
@Setter
public class RestaurantInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 문의 식별자

    @Column(nullable = false)
    private String content; // 문의 내용

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 문의 작성시간

    private LocalDateTime deletedAt;    // 문의 삭제시간

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;  // 음식점 객체 정보

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;  // 문의 작성자

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reply_id")
    private RestaurantInquiryReply reply; // 문의 답변

    public RestaurantInquiry(Member writer, Restaurant restaurant, String content) {
        this.writer = writer;
        this.restaurant = restaurant;
        this.content = content;
    }

    public void addReply(RestaurantInquiryReply reply) {
        this.reply = reply;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}