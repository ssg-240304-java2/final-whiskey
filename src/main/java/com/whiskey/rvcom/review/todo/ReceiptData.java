package com.whiskey.rvcom.review.todo;

import java.time.LocalDateTime;
import java.util.List;

public class ReceiptData {
    private Long id;

    private String storeName;
    private String storeAddress;

    // paymentInfo.date.text
    // paymentInfo.time.text
    // todo. 위 2개 필드 결합하여 LocalDateTime으로 변환해야 함
    private LocalDateTime paidAt;

    // 품목별 구매 정보
    private List<PaidItem> paidItems;

    // 총결제액
    private int totalPrice;
}
