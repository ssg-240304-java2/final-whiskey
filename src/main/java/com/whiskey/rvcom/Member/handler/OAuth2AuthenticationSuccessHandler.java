package com.whiskey.rvcom.Member.handler;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * OAuth2 로그인 성공 시 처리하는 핸들러 클래스입니다.
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final SocialLoginService socialLoginService;

    public OAuth2AuthenticationSuccessHandler(SocialLoginService socialLoginService) {
        this.socialLoginService = socialLoginService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> userAttributes = oAuth2User.getAttributes();

        String loginId = null;
        LoginType loginType;

        String registrationId = getClientRegistrationId(authentication);

        switch (registrationId) {
            case "google":
                loginId = (String) userAttributes.get("sub");  // Google의 경우 sub를 식별자로 사용
                loginType = LoginType.GOOGLE;
                break;
            case "naver":
                loginId = (String) userAttributes.get("id");  // Naver의 경우 id를 식별자로 사용
                loginType = LoginType.NAVER;
                break;
            default:
                throw new IllegalStateException("Unknown registrationId: " + registrationId);
        }

        try {
            Member member = socialLoginService.findMemberByLoginIdAndLoginType(loginId, loginType);

            // 이미 존재하는 사용자는 로그인 처리
            request.getSession().setAttribute("member", member);
            getRedirectStrategy().sendRedirect(request, response, "/mainPage");
        } catch (Exception e) {
            // 사용자가 존재하지 않으면 회원가입 페이지로 리디렉션
            request.getSession().setAttribute("userAttributes", userAttributes);
            request.getSession().setAttribute("loginType", loginType.name());
            request.getSession().setAttribute("loginId", loginId);  // 식별자 저장
            getRedirectStrategy().sendRedirect(request, response, "/register_social");
        }
    }

    private String getClientRegistrationId(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (attributes.containsKey("sub")) {
            return "google";
        } else if (attributes.containsKey("id")) {
            return "naver";
        }
        throw new IllegalStateException("Unknown OAuth2 provider");
    }
}
