package com.whiskey.rvcom.Member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final String naverClientId;
    private final String naverClientSecret;
    private final String googleClientId;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 세션 무효화
        request.getSession().invalidate();

        // 소셜 로그인 제공자의 토큰 무효화 로직 유지
        String accessToken = (String) request.getSession().getAttribute("accessToken");
        String provider = (String) request.getSession().getAttribute("provider");

        if (accessToken != null && provider != null) {
            switch (provider.toLowerCase()) {
                case "naver":
                    invalidateNaverToken(accessToken);
                    break;
                case "google":
                    invalidateGoogleToken(accessToken);
                    break;
            }
        }

        // 로그아웃 후 리다이렉트
        response.sendRedirect("/");
    }

    private void invalidateNaverToken(String accessToken) {
        String url = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "delete")
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER")
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, null, String.class);
    }

    private void invalidateGoogleToken(String accessToken) {
        String url = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/revoke")
                .queryParam("token", accessToken)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, null, String.class);
    }
}
