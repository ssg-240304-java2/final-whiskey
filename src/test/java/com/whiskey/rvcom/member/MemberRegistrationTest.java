package com.whiskey.rvcom.member;

import com.whiskey.rvcom.Member.service.MemberManagementService;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRegistrationTest {

    @Autowired
    private MemberManagementService memberManagementService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    private MockHttpSession session;
    private Model model;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
        model = new BindingAwareModelMap();
    }

    @Test
    public void 회원가입_성공_테스트() {
        // 회원가입을 위한 데이터 설정
        String name = "TestUser";
        String nickname = "TestNick";
        String loginId = "testUser@example.com";
        String email = "testUser@example.com";
        String rawPassword = "testPassword";  // 원본 비밀번호
        LoginType loginType = LoginType.BASIC;

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 회원가입 호출 (비밀번호는 인코딩된 상태로 전달)
        Member savedMember = memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, loginType);

        // 결과 검증
        assertNotNull(savedMember, "회원가입이 정상적으로 이루어져야 합니다.");
        assertEquals(loginId, savedMember.getLoginId(), "저장된 회원의 로그인 ID가 일치해야 합니다.");
        assertEquals(nickname, savedMember.getNickname(), "닉네임은 인코딩되지 않고 원본 그대로 저장되어야 합니다.");
        assertTrue(passwordEncoder.matches(rawPassword, savedMember.getPassword()), "비밀번호가 인코딩되어 저장되고, 원본 비밀번호와 일치해야 합니다.");
    }

    @Test
    public void 회원가입_실패_테스트_중복_아이디() {
        // 중복된 아이디로 회원가입 시도
        String name = "TestUser";
        String nickname = "TestNick";
        String loginId = "testUser@example.com";
        String email = "testUser@example.com";
        String rawPassword = "testPassword";
        LoginType loginType = LoginType.BASIC;

        // 비밀번호를 인코딩한 후 회원가입 호출
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 첫 번째 회원가입은 성공해야 함
        memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, loginType);

        // 중복된 아이디로 회원가입 시도
        assertThrows(IllegalArgumentException.class, () -> {
            memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, loginType);
        }, "중복된 로그인 ID로 회원가입 시 예외가 발생해야 합니다.");
    }

    @Test
    public void 로그인_성공_테스트() {
        // 회원가입 먼저 수행
        회원가입_성공_테스트();

        String loginId = "testUser@example.com";
        String rawPassword = "testPassword";

        // 로그인 요청을 위한 데이터 설정
        ResponseEntity<Void> response = restTemplate.postForEntity("/login?loginId=" + loginId + "&password=" + rawPassword, null, Void.class);

        // 로그인 후 리다이렉션 상태와 URL 확인
        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "로그인이 성공적으로 이루어져야 합니다.");
        assertNotNull(response.getHeaders().getLocation(), "리다이렉션 URL이 존재해야 합니다.");
        assertTrue(response.getHeaders().getLocation().toString().contains("/mainPage"), "로그인 성공 후 '/mainPage'로 리다이렉션되어야 합니다.");
    }

    @Test
    public void 로그아웃_성공_테스트() {
        // 로그인 먼저 수행
        로그인_성공_테스트();

        // 로그아웃 요청
        ResponseEntity<Void> response = restTemplate.postForEntity("/logout", null, Void.class);

        // 로그아웃 후 리다이렉션 상태와 URL 확인
        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "로그아웃이 성공적으로 이루어져야 합니다.");
        assertNotNull(response.getHeaders().getLocation(), "리다이렉션 URL이 존재해야 합니다.");
        assertTrue(response.getHeaders().getLocation().toString().contains("/login"), "로그아웃 후 '/login'으로 리다이렉션되어야 합니다.");
    }

}
