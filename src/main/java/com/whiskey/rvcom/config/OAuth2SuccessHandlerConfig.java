package com.whiskey.rvcom.config;

import com.whiskey.rvcom.Member.handler.OAuth2AuthenticationSuccessHandler;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2SuccessHandlerConfig {

    private SocialLoginService socialLoginService;

    @Autowired
    public void setSocialLoginService(SocialLoginService socialLoginService) {
        this.socialLoginService = socialLoginService;
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(socialLoginService);
    }
}
