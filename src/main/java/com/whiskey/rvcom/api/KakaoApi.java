package com.whiskey.rvcom.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakao")
public class KakaoApi {

//    private static final Logger log = LoggerFactory.getLogger(KakaoApi.class);
    @Value("${kakao.apikey}")
    private String apiKey;

    @GetMapping("/apiKey")
    public String getKakaoApiKey() {
        return apiKey;
    }
}
