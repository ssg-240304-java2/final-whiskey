package com.whiskey.rvcom.Member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 에러 메시지를 쿼리 파라미터로 전달
//        setDefaultFailureUrl("/login?error=아이디 또는 비밀번호가 다릅니다.");
        setDefaultFailureUrl("/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
