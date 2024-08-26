package com.whiskey.rvcom.businessregister.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BusinessRequestHead {

    private List<Business> businesses;

}

@Getter
@Setter
@ToString
class Business {
    private String b_no; // 사업자등록증 번호
    private String start_dt; // 개업일자
    private String p_nm; // 대표자명
}