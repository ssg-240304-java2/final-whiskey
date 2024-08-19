package com.whiskey.rvcom.review.receipt;

import com.whiskey.rvcom.entity.receipt.*;
import com.whiskey.rvcom.util.ocr.model.receipt.ReceiptResponse;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReceiptMapper {
    public ReceiptData toEntity(ReceiptResponse dto) {
        ReceiptData receiptData = new ReceiptData();

        // Store 정보 설정 (null 체크)
        String storeName = Optional.ofNullable(dto.getStoreInfo())
                .map(ReceiptResponse.StoreInfo::getName)
                .map(ReceiptResponse.Name::getText)
                .orElse("Unknown Store");
        receiptData.setStoreName(storeName);

        String storeAddress = Optional.ofNullable(dto.getStoreInfo())
                .map(ReceiptResponse.StoreInfo::getAddresses)
                .filter(addresses -> !addresses.isEmpty())
                .map(addresses -> addresses.get(0).getText())
                .orElse("Unknown Address");
        receiptData.setStoreAddress(storeAddress);

        // 결제 시간 설정 (null 체크 및 변환)
        String dateText = Optional.ofNullable(dto.getPaymentInfo())
                .map(ReceiptResponse.PaymentInfo::getDate)
                .map(ReceiptResponse.Date::getText)
                .orElse(null);

        String timeText = Optional.ofNullable(dto.getPaymentInfo())
                .map(ReceiptResponse.PaymentInfo::getTime)
                .map(ReceiptResponse.Time::getText)
                .orElse(null);

        LocalDateTime paidAt = null;
        if (dateText != null && timeText != null) {
            try {
                LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalTime time = LocalTime.parse(timeText.replace(" ", ""), DateTimeFormatter.ofPattern("HH:mm:ss"));
                paidAt = LocalDateTime.of(date, time);
            } catch (Exception e) {
                paidAt = LocalDateTime.now(); // 기본값으로 현재 시간 설정
            }
        } else {
            paidAt = LocalDateTime.now(); // 기본값으로 현재 시간 설정
        }
        receiptData.setPaidAt(paidAt);

        // 품목(Item) 변환 (null 체크)
        List<PaidItem> paidItems = new ArrayList<>();
        Optional.ofNullable(dto.getSubResults())
                .ifPresent(subResults -> subResults.forEach(subResult -> {
                    subResult.getItems().forEach(item -> {
                        String itemName = Optional.ofNullable(item.getName())
                                .map(ReceiptResponse.Name::getText)
                                .orElse("Unknown Item");

                        int price = Optional.ofNullable(item.getPrice().getPrice())
                                .map(ReceiptResponse.PriceDetail::getText)
                                .map(text -> Integer.parseInt(text.replace(",", "")))
                                .orElse(0);

                        int quantity = Optional.ofNullable(item.getCount())
                                .map(ReceiptResponse.Count::getText)
                                .map(Integer::parseInt)
                                .orElse(1);

                        PaidItem paidItem = new PaidItem();
                        paidItem.setItemName(itemName);
                        paidItem.setPrice(price);
                        paidItem.setQuantity(quantity);
                        paidItems.add(paidItem);
                    });
                }));

        receiptData.setPaidItems(paidItems);

        // 총 결제 금액 설정 (null 체크)
        int totalPrice = Optional.ofNullable(dto.getTotalPrice())
                .map(ReceiptResponse.TotalPrice::getText)
                .map(text -> Integer.parseInt(text.replace(",", "")))
                .orElse(0);
        receiptData.setTotalPrice(totalPrice);

        return receiptData;
    }
}
