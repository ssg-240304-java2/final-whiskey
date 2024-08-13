package com.whiskey.rvcom.member;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SocialRegisterControllerTest {

    @Autowired
    private SocialLoginService socialLoginService;

    private MockHttpSession session;
    private Model model;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
        model = new BindingAwareModelMap();
    }

    @Test
    public void testRegisterSocial_Success() {
        String nickname = "testNickname";
        String loginId = "116516685741003489657";
        String loginType = "GOOGLE";
        String name = "Song Jaehyeok";
        String email = "speter650@gmail.com";

        // Simulate social login
        socialLoginService.handleSocialLogin(session, loginId, name, email, loginType);

        // Simulate user registration
        session.setAttribute("nickname", nickname);
        String result = registerSocial(nickname, session, model);

        assertEquals("redirect:/login", result);
        Member member = (Member) session.getAttribute("member");
        assertEquals(nickname, member.getNickname());
    }

    @Test
    public void testRegisterSocial_MemberAlreadyExists() {
        String nickname = "testNickname";
        String loginId = "116516685741003489657";
        String loginType = "GOOGLE";
        String name = "Song Jaehyeok";
        String email = "speter650@gmail.com";

        // 기존 데이터 삭제
        Member existingMember = socialLoginService.findMemberByLoginIdAndLoginType(loginId, LoginType.GOOGLE);
        if (existingMember != null) {
            memberRepository.delete(existingMember);
        }

        // Save the member first to simulate an existing member
        existingMember = new Member();
        existingMember.setLoginId(loginId);
        existingMember.setNickname(nickname);
        existingMember.setName(name);
        existingMember.setEmail(email);
        existingMember.setLoginType(LoginType.GOOGLE);
        existingMember.setRole(Role.USER);
        existingMember.setActive(true);
        socialLoginService.save(existingMember);

        // Simulate social login
        socialLoginService.handleSocialLogin(session, loginId, name, email, loginType);

        // Attempt to register again
        String result = registerSocial(nickname, session, model);

        assertEquals("redirect:/login", result);
    }


    private String registerSocial(String nickname, MockHttpSession session, Model model) {
        // This is a simplified version of the original registerSocial method for testing purposes
        String loginId = (String) session.getAttribute("loginId");
        String loginTypeStr = (String) session.getAttribute("loginType");
        Map<String, Object> userAttributes = (Map<String, Object>) session.getAttribute("userAttributes");

        if (loginId == null || loginTypeStr == null || userAttributes == null) {
            model.addAttribute("error", "세션 정보가 없습니다. 다시 로그인해 주세요.");
            return "redirect:/login";
        }

        LoginType loginType = LoginType.valueOf(loginTypeStr.toUpperCase());
        Member existingMember = socialLoginService.findMemberByLoginIdAndLoginType(loginId, loginType);
        if (existingMember != null) {
            model.addAttribute("error", "이미 존재하는 회원입니다.");
            return "register_social";
        }

        Member member = new Member();
        member.setLoginId(loginId);
        member.setNickname(nickname);
        member.setName((String) userAttributes.get("name"));
        member.setEmail((String) userAttributes.get("email"));
        member.setLoginType(loginType);
        member.setRole(Role.USER);
        member.setActive(true);
        socialLoginService.save(member);
        session.setAttribute("member", member);
        return "redirect:/success";
    }
}
