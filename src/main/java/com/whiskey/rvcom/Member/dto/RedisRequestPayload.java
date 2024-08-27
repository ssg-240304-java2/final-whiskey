package com.whiskey.rvcom.Member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class RedisRequestPayload {
    private String key;
    private String value;
}