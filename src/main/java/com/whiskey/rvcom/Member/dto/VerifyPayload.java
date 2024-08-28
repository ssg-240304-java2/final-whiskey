package com.whiskey.rvcom.Member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerifyPayload {
    private String email;
    private String code;
}