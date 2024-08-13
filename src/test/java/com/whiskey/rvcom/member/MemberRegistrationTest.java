package com.whiskey.rvcom.member;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom. Member.service.MemberManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberRegistrationTest {

    @Autowired
    private MemberManagementService memberManagementService;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

        // 중복된 아이디로 회원가입 시도
        assertThrows(IllegalArgumentException.class, () -> {
            memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, loginType);
        }, "중복된 로그인 ID로 회원가입 시 예외가 발생해야 합니다.");
    }

    @Test
    public void 로그인_성공_테스트() {
        String loginId = "testUser@example.com";
        String rawPassword = "testPassword";

        // 로그인 호출
        String result = login(loginId, rawPassword, session, model);

        // 로그인 검증
        assertEquals("redirect:/success", result, "로그인이 성공적으로 이루어져야 합니다.");
        assertNotNull(session.getAttribute("member"), "로그인 후 세션에 멤버가 저장되어야 합니다.");
    }

    @Test
    public void 로그아웃_성공_테스트() {
        // 로그인 먼저 수행
        로그인_성공_테스트();

        // 로그아웃 호출
        String result = logout(session);

        // 로그아웃 검증
        assertEquals("redirect:/login", result, "로그아웃 후 로그인 페이지로 리다이렉트되어야 합니다.");

        // 세션이 무효화되었는지 확인
        assertThrows(IllegalStateException.class, () -> {
            session.getAttribute("member");
        }, "세션이 무효화된 후에는 세션의 속성에 접근할 수 없습니다.");
    }

    private String login(String loginId, String password, MockHttpSession session, Model model) {
        try {
            Member member = memberManagementService.findByLoginId(loginId);
            boolean passwordMatches = passwordEncoder.matches(password, member.getPassword());

            if (passwordMatches) {
                session.setAttribute("member", member);
                return "redirect:/success";
            } else {
                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "존재하지 않는 회원입니다.");
        }

        return "login";
    }

    private String logout(MockHttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
