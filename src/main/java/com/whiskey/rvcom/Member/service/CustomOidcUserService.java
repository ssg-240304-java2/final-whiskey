package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.Member.oauth2.OAuth2Attributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {

        try {
            // DefaultOAuth2UserService를 사용하여 OAuth2User를 로드
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(request);

            // 클라이언트 등록 ID를 가져옴
            String registrationId = request.getClientRegistration().getRegistrationId();

            // 사용자 이름 속성 이름을 가져옴
            String userNameAttributeName = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            // OAuth2Attributes를 생성하여 사용자 속성 맵핑
            OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

            // DefaultOAuth2User를 생성하여 반환
            return (OidcUser) new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey()
            );
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            // 일반 예외 처리
            OAuth2Error oauth2Error = new OAuth2Error("server_error", "Failed to load user", null);
            throw new OAuth2AuthenticationException(oauth2Error, e);
        }
    }
}
