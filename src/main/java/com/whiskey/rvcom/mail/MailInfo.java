package com.whiskey.rvcom.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MailInfo { // 이메일 정보를 담는 DTO

    private String to; // 이메일 받을 사람
    private String subject; // 제목
    private String text; // 본문
}