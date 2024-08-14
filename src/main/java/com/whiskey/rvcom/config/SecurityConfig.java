package com.whiskey.rvcom.config;

import com.whiskey.rvcom.Member.service.CustomOAuth2AuthService;
import com.whiskey.rvcom.Member.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2AuthService customOAuth2AuthService;
    private final CustomOidcUserService customOidcUserService;

    public SecurityConfig(CustomOAuth2AuthService customOAuth2AuthService,
                          CustomOidcUserService customOidcUserService) {
        this.customOAuth2AuthService = customOAuth2AuthService;
        this.customOidcUserService = customOidcUserService;
    }

    //일단 permitall로 설정후 나중에

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                ) // 세션 관리 설정
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/mainPage", "/login", "/register_basic", "/register_social", "/checkLoginId", "/css/**", "/js/**", "/images/**", "/static/**")
//                        .permitAll() // 특정 경로에 대한 접근 허용
//                        .requestMatchers("/user/**").hasRole("USER") // 유저만 접근 가능
//                        .requestMatchers("/owner/**").hasRole("OWNER") // 점주만 접근 가능
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능
//                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
//              )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청에 대한 접근 허용
                )
                .formLogin(form -> form
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/perform_login") // 로그인 처리 URL
                        .defaultSuccessUrl("/mainPage", true) // 로그인 성공 후 이동할 기본 페이지
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll() // 로그인 관련 요청은 모두 허용
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2AuthService) // OAuth2 사용자 서비스 설정
                                .oidcUserService(customOidcUserService) // OIDC 사용자 서비스 설정
                        )
                        .defaultSuccessUrl("/mainPage", true) // OAuth2 로그인 성공 후 이동할 기본 페이지
                        .failureUrl("/login?error=true") // OAuth2 로그인 실패 시 이동할 페이지
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정
                        .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 후 이동할 페이지
                        .permitAll() // 로그아웃 관련 요청은 모두 허용
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코더 빈 등록
    }
}
