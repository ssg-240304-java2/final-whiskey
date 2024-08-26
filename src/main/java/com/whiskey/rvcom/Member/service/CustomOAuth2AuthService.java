package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.Member.oauth2.CustomOAuth2User;
import com.whiskey.rvcom.Member.oauth2.OAuth2Attributes;
import com.whiskey.rvcom.entity.member.Member;
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
import java.io.PrintWriter;
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
        // OAuth2User 정보 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 등록된 OAuth2 클라이언트의 식별자와 사용자 이름 속성명 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2Attributes 생성
        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        if (attributes == null || attributes.getAttributes() == null) {
            log.error("OAuth2Attributes is null. This should not happen.");
            throw new IllegalStateException("Failed to load user attributes from OAuth2 provider.");
        }

        // 데이터베이스에서 사용자를 검색
        Member member = memberRepository.findByLoginIdAndLoginType(attributes.getOauthId(), attributes.getLoginType());

        // 사용자 정보가 없는 경우 회원 가입 페이지로 리다이렉트
        if (member == null) {
            log.info("Member not found in database, redirecting to registration.");

            try {
                session.invalidate();  // 기존 세션 무효화
                HttpSession newSession = session; // 기존 세션 객체 그대로 사용
                newSession.setAttribute("userAttributes", attributes.getAttributes());
                newSession.setAttribute("loginType", attributes.getLoginType().toString());
                newSession.setAttribute("loginId", attributes.getOauthId());
                response.sendRedirect("/register_social");
            } catch (IOException e) {
                log.error("Redirect failed", e);
            }

            return new CustomOAuth2User(attributes.getAttributes(), attributes.getNameAttributeKey(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }

        // 계정이 비활성화된 경우 알럿을 띄우고 로그인 중단
        if (!member.isActive()) {
            log.info("Attempted login with deactivated account: {}", member.getLoginId());
            try {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('해당 계정은 비활성화되어 있습니다. 관리자에게 문의하세요.'); location.href='/login';</script>");
                out.flush();
            } catch (IOException e) {
                log.error("Failed to send alert for deactivated account", e);
            }
            return null; // null 반환하여 인증 실패 처리
        }

        // 사용자가 있는 경우 권한과 함께 OAuth2User 반환
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));
        session.setAttribute("member", member); // 세션에 member 객체 저장
        return new CustomOAuth2User(attributes.getAttributes(), attributes.getNameAttributeKey(), authorities);
    }
}

