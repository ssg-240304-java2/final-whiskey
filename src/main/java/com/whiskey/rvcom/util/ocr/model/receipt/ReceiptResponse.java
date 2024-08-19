package com.whiskey.rvcom.util.ocr.model.receipt;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class ReceiptResponse {
    private String version;
    private String requestId;
    private long timestamp;
    private List<Image> images;

    @Getter
    @Setter
    public class Image {
        private Receipt receipt;
        private String uid;
        private String name;
        private String inferResult;
        private String message;
        private ValidationResult validationResult;
    }

    @Getter
    @Setter
    public class Receipt {
        private Meta meta;
        private Result result;
    }

    @Getter
    @Setter
    public class Meta {
        private String estimatedLanguage;
    }

    @Getter
    @Setter
    public class Result {
        private StoreInfo storeInfo;
        private PaymentInfo paymentInfo;
        private List<SubResult> subResults;
        private TotalPrice totalPrice;
    }

    @Getter
    @Setter
    public class StoreInfo {
        private Name name;
        private Name subName;
        private Name bizNum;
        private List<Address> addresses;
        private List<Tel> tel;
    }

    @Getter
    @Setter
    public class Name {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class Address {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class Tel {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class Formatted {
        private String value;
        private String year;
        private String month;
        private String day;
        private String hour;
        private String minute;
        private String second;
    }

    @Getter
    @Setter
    public class BoundingPolys {
        private List<Vertex> vertices;
    }

    @Getter
    @Setter
    public class Vertex {
        private double x;
        private double y;
    }

    @Getter
    @Setter
    public class PaymentInfo {
        private Date date;
        private Time time;
        private CardInfo cardInfo;
        private ConfirmNum confirmNum;
    }

    @Getter
    @Setter
    public class Date {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class Time {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class CardInfo {
        private Name company;
        private Name number;
    }

    @Getter
    @Setter
    public class ConfirmNum {
        private String text;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class SubResult {
        private List<Item> items;
    }

    @Getter
    @Setter
    public class Item {
        private Name name;
        private Count count;
        private Price price;
    }

    @Getter
    @Setter
    public class Count {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class Price {
        private PriceDetail price;
        private PriceDetail unitPrice;
    }

    @Getter
    @Setter
    public class PriceDetail {
        private String text;
        private Formatted formatted;
        private List<BoundingPolys> boundingPolys;
    }

    @Getter
    @Setter
    public class TotalPrice {
        private PriceDetail price;
    }

    @Getter
    @Setter
    public class ValidationResult {
        private String result;
    }
}
