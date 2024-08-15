package com.whiskey.rvcom.config;

import com.whiskey.rvcom.Member.service.CustomOAuth2AuthService;
import com.whiskey.rvcom.Member.service.CustomOidcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Slf4j
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                ) // 세션 관리 설정
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/mainPage", "/login", "/register_basic", "/register_social", "/checkLoginId", "/css/**", "/js/**", "/images/**", "/static/**")
//                        .permitAll() // 특정 경로에 대한 접근 허용
//                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
                                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/mainPage", true)
                        .failureUrl("/login?error=true")
                        .successHandler((request, response, authentication) -> {
                            log.info("User {} has successfully logged in.", authentication.getName());
                            log.info("Authorities: {}", authentication.getAuthorities());
                            log.info("Is authenticated: {}", authentication.isAuthenticated());
                            log.info("User Roles after setting context: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.info("User Roles: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                            response.sendRedirect("/mainPage");
                        })
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2AuthService) // OAuth2 사용자 서비스 설정
                                .oidcUserService(customOidcUserService) // OIDC 사용자 서비스 설정
                        )
                        .defaultSuccessUrl("/mainPage", true) // OAuth2 로그인 성공 후 이동할 기본 페이지
                        .failureUrl("/login?error=true") // OAuth2 로그인 실패 시 이동할 페이지
                        .successHandler((request, response, authentication) -> {
                            log.info("User {} has successfully logged in via OAuth2.", authentication.getName());

                            // 세션에 인증 정보 추가
                            request.getSession().setAttribute("isAuthenticated", true);

                            // userRole을 문자열로 변환하여 세션에 저장
                            String userRole = authentication.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.joining(","));
                            request.getSession().setAttribute("userRole", userRole);

                            response.sendRedirect("/mainPage");
                        })

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
