package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.Member.oauth2.CustomOAuth2User;
import com.whiskey.rvcom.Member.oauth2.OAuth2Attributes;
import com.whiskey.rvcom.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2AuthService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession session;
    private final HttpServletResponse response;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 사용자 정보로 데이터베이스에서 회원 조회
        Member member = memberRepository.findByLoginIdAndLoginType(attributes.getOauthId(), attributes.getLoginType());

        // Null 체크 추가
        if (attributes == null || attributes.getAttributes() == null) {
            log.error("OAuth2Attributes is null. This should not happen.");
            throw new IllegalStateException("Failed to load user attributes from OAuth2 provider.");
        }

        if (member == null) {
            // 사용자가 없을 경우, 회원가입 페이지로 리다이렉트
            try {
                session.setAttribute("userAttributes", attributes.getAttributes());
                session.setAttribute("loginType", attributes.getLoginType().toString());
                session.setAttribute("loginId", attributes.getOauthId());
                response.sendRedirect("/register_social");
            } catch (IOException e) {
                log.error("Redirect failed", e);
            }
            // 사용자에게 기본 권한을 가진 OAuth2User 객체 반환
            return new CustomOAuth2User(attributes.getAttributes(), attributes.getNameAttributeKey(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }

        // 기존 회원이 있는 경우, 사용자 정보와 권한 생성하여 반환
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));

        return new CustomOAuth2User(attributes.getAttributes(), attributes.getNameAttributeKey(), authorities);
    }
}


