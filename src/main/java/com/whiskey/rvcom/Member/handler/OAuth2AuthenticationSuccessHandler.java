package com.whiskey.rvcom.Member.handler;

import com.whiskey.rvcom.Member.service.SocialLoginService;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
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
                loginId = (String) userAttributes.get("sub");
                loginType = LoginType.GOOGLE;
                break;
            case "naver":
                loginId = (String) userAttributes.get("id");
                loginType = LoginType.NAVER;
                break;
            default:
                throw new IllegalStateException("Unknown registrationId: " + registrationId);
        }

        try {
            Member member = socialLoginService.findMemberByLoginIdAndLoginType(loginId, loginType);

            request.getSession().invalidate();  // 이전 세션 무효화
            HttpSession newSession = request.getSession(true);  // 새로운 세션 생성

            newSession.setAttribute("member", member);
            newSession.setAttribute("isAuthenticated", true);
            newSession.setAttribute("userRole", member.getRole().name());
            newSession.setAttribute("isSocialLogin", loginType != LoginType.BASIC); // 소셜 로그인 여부 설정

            log.info("Session ID after successful authentication: {}", newSession.getId());
            log.info("Session attributes set: isAuthenticated={}, userRole={}",
                    newSession.getAttribute("isAuthenticated"),
                    newSession.getAttribute("userRole"));

            getRedirectStrategy().sendRedirect(request, response, "/mypage");
        } catch (Exception e) {
            log.error("Member not found, redirecting to registration page.", e);

            request.getSession().invalidate();  // 이전 세션 무효화
            HttpSession newSession = request.getSession(true);  // 새로운 세션 생성

            newSession.setAttribute("userAttributes", userAttributes);
            newSession.setAttribute("loginType", loginType.name());
            newSession.setAttribute("loginId", loginId);
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
