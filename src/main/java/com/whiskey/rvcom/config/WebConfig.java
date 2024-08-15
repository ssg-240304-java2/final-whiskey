package com.whiskey.rvcom.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                if (modelAndView != null) {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
                        String userRole = (String) session.getAttribute("userRole");

                        // 로그 추가
                        log.info("postHandle - isAuthenticated: {}, userRole: {}", isAuthenticated, userRole);

                        modelAndView.addObject("isAuthenticated", isAuthenticated);
                        modelAndView.addObject("userRole", userRole);
                    }
                }
            }
        });
    }
}

