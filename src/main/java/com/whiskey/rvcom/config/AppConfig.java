package com.whiskey.rvcom.config;


import com.whiskey.rvcom.Member.handler.CustomLogoutSuccessHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // 네이버 client ID
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    // 네이버 client secret
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    // 구글 client ID
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 커스텀 로그아웃 성공 핸들러 빈을 생성
     *
     * @return CustomLogoutSuccessHandler 인스턴스
     */
    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(naverClientId, naverClientSecret, googleClientId);
    }
}
