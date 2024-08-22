package com.whiskey.rvcom.businessregister.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistInfo { // 입점신청서 값 받기용 DTO

    private String name;
    private String phone;
    private String address;
    private String category;
    private Long memberId;
}
