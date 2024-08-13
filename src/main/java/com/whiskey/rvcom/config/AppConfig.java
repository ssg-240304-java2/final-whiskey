package com.whiskey.rvcom.config;


import com.whiskey.rvcom.Member.handler.CustomLogoutSuccessHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // 네이버 client ID
    @Value("DXEMF06fL20Py67dy9pp")
    private String naverClientId;

    // 네이버 client secret
    @Value("89Y9atEqvW")
    private String naverClientSecret;

    // 구글 client ID
    @Value("62790710914-0uj632naot18b9s7vq28inn1122l506o.apps.googleusercontent.com")
    private String googleClientId;

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
