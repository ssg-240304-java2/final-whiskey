package com.whiskey.rvcom.Member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private String to;
    private String subject;
    private String text;
}
