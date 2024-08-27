package com.whiskey.rvcom.Member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = "/login?error=true";

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2Exception = (OAuth2AuthenticationException) exception;
            if ("user_registration_required".equals(oAuth2Exception.getError().getErrorCode())) {
                redirectUrl = "/register_social";
            }
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
