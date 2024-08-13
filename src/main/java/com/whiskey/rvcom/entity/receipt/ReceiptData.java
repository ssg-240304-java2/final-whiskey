package com.whiskey.rvcom.entity.receipt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "tbl_receipt_data")
@Entity
public class ReceiptData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String storeAddress;

    // paymentInfo.date.text
    // paymentInfo.time.text
    // todo. 위 2개 필드 결합하여 LocalDateTime으로 변환해야 함
    @Column(nullable = false)
    private LocalDateTime paidAt;

    // 품목별 구매 정보
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receipt_data_id")
    private List<PaidItem> paidItems;

    // 총결제액
    @Column(nullable = false)
    private int totalPrice;
}
