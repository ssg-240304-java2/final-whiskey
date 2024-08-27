package com.whiskey.rvcom.Member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPayload {
    private String email;
    private String code;
}