package com.whiskey.rvcom.review.receipt;

import com.whiskey.rvcom.entity.receipt.*;
import com.whiskey.rvcom.util.ocr.model.receipt.ReceiptResponse;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReceiptMapper {
    public ReceiptData toEntity(ReceiptResponse dto) {
        ReceiptData receiptData = new ReceiptData();

        // 1. Store 정보 설정 (null 체크)
        String storeName = Optional.ofNullable(dto.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0).getReceipt().getResult().getStoreInfo().getName().getText())
                .orElse("Unknown Store");
        receiptData.setStoreName(storeName);

        String storeAddress = Optional.ofNullable(dto.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0).getReceipt().getResult().getStoreInfo().getAddresses())
                .filter(addresses -> !addresses.isEmpty())
                .map(addresses -> addresses.get(0).getText())
                .orElse("Unknown Address");
        receiptData.setStoreAddress(storeAddress);

        // 2. paidAt 설정 (null 체크 및 변환)
        String dateText = Optional.ofNullable(dto.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0).getReceipt().getResult().getPaymentInfo().getDate().getText())
                .orElse(null);

        String timeText = Optional.ofNullable(dto.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0).getReceipt().getResult().getPaymentInfo().getTime().getText())
                .orElse(null);

        LocalDateTime paidAt = null;
        if (dateText != null && timeText != null) {
            try {
                LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalTime time = LocalTime.parse(timeText.replace(" ", ""), DateTimeFormatter.ofPattern("HH:mm"));
                paidAt = LocalDateTime.of(date, time);
            } catch (Exception e) {
                // 예외 발생 시 기본값 설정 또는 로그 출력
                paidAt = LocalDateTime.now(); // 기본값으로 현재 시간 설정
            }
        } else {
            paidAt = LocalDateTime.now(); // 기본값으로 현재 시간 설정
        }
        receiptData.setPaidAt(paidAt);

        // 3. PaidItems 변환 (null 체크)
        List<PaidItem> paidItems = new ArrayList<>();
        Optional.ofNullable(dto.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0).getReceipt().getResult().getSubResults())
                .ifPresent(subResults -> subResults.forEach(subResult -> {
                    subResult.getItems().forEach(item -> {
                        String itemName = Optional.ofNullable(item.getName()).map(ReceiptResponse.Name::getText).orElse("Unknown Item");
                        int price = Optional.ofNullable(item.getPrice().getPrice())
                                .map(priceDetail -> Integer.parseInt(priceDetail.getText().replace(",", "")))
                                .orElse(0);
                        int quantity = Optional.ofNullable(item.getCount()).map(ReceiptResponse.Count::getText).map(Integer::parseInt).orElse(1);

                        PaidItem paidItem = new PaidItem();
                        paidItem.setItemName(itemName);
                        paidItem.setPrice(price);
                        paidItem.setQuantity(quantity);
                        paidItems.add(paidItem);
                    });
                }));

        receiptData.setPaidItems(paidItems);

        // 4. TotalPrice 설정 (null 체크)
        int totalPrice = Optional.ofNullable(dto.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0).getReceipt().getResult().getTotalPrice().getPrice())
                .map(priceDetail -> Integer.parseInt(priceDetail.getText().replace(",", "")))
                .orElse(0); // 기본값 0으로 설정
        receiptData.setTotalPrice(totalPrice);

        return receiptData;
    }
}
