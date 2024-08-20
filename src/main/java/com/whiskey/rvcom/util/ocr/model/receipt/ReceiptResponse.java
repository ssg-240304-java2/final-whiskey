package com.whiskey.rvcom.util.ocr.model.receipt;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class ReceiptResponse {
    private StoreInfo storeInfo;
    private PaymentInfo paymentInfo;
    private List<SubResult> subResults;
    private TotalPrice totalPrice;
    private List<SubTotal> subTotal;

    @Getter
    @Setter
    @ToString
    public class StoreInfo {
        private Name name;
        private BizNum bizNum;
        private List<Address> addresses;
        private List<Tel> tel;
    }

    @Getter
    @Setter
    @ToString
    public class Name {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class BizNum {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class Address {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class Tel {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class PaymentInfo {
        private Date date;
        private Time time;
        private CardInfo cardInfo;
    }

    @Getter
    @Setter
    @ToString
    public class Date {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class Time {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class CardInfo {
        private Company company;
        private Number number;
    }

    @Getter
    @Setter
    @ToString
    public class Company {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class Number {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class SubResult {
        private List<Item> items;
    }

    @Getter
    @Setter
    @ToString
    public class Item {
        private Name name;
        private Count count;
        private Price price;
    }

    @Getter
    @Setter
    @ToString
    public class Count {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class Price {
        private PriceDetail price;
        private UnitPrice unitPrice;
    }

    @Getter
    @Setter
    @ToString
    public class PriceDetail {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class UnitPrice {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class TotalPrice {
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public class SubTotal {
        private List<TaxPrice> taxPrice;
    }

    @Getter
    @Setter
    @ToString
    public class TaxPrice {
        private String text;
    }
}
