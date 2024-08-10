package com.whiskey.rvcom.entity.restaurant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RestaurantCategory {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    ASIAN("아시안음식"),
    MEXICAN("멕시칸음식"),
    INDIAN("인도음식"),
    ITALIAN("이탈리안음식"),
    SEAFOOD ("해산물"),
    MIDDLE_EASTERN ("중동음식"),
    FUSION("퓨전요리"),
    FAST_FOOD("패스트푸드"),
    CAFE("카페"),
    DESSERT ("디저트"),
    BAKERY("베이커리"),
    BAR("주점"),
    PUB("펍"),
    ETC("기타");

    private final String title;
}