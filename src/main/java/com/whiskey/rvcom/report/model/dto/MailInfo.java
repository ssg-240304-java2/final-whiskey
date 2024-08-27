package com.whiskey.rvcom.report.model.dto;

import com.whiskey.libs.rest.request.RequestMethod;
import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.businessregister.model.dto.BusinessRequestHead;
import com.whiskey.rvcom.businessregister.model.dto.MyResponseBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MailInfo { // 이메일 정보를 담는 DTO

    private String to; // 이메일 받을 사람
    private String subject; // 제목
    private String text; // 본문
}