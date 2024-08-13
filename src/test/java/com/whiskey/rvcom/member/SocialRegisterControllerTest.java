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

//    private String registerSocial(String nickname, MockHttpSession session, Model model) {
//        // This is a simplified version of the original registerSocial method for testing purposes
//        String loginId = (String) session.getAttribute("loginId");
//        String loginTypeStr = (String) session.getAttribute("loginType");
//        Map<String, Object> userAttributes = (Map<String, Object>) session.getAttribute("userAttributes");
//
//        if (loginId == null || loginTypeStr == null || userAttributes == null) {
//            model.addAttribute("error", "세션 정보가 없습니다. 다시 로그인해 주세요.");
//            return "redirect:/login";
//        }
//
//        LoginType loginType = LoginType.valueOf(loginTypeStr.toUpperCase());
//        Member existingMember = socialLoginService.findMemberByLoginIdAndLoginType(loginId, loginType);
//        if (existingMember != null) {
//            model.addAttribute("error", "이미 존재하는 회원입니다.");
//            return "register_social";
//        }
//
//        Member member = new Member();
//        member.setLoginId(loginId);
//        member.setNickname(nickname);
//        member.setName((String) userAttributes.get("name"));
//        member.setEmail((String) userAttributes.get("email"));
//        member.setLoginType(loginType);
//        member.setRole(Role.USER);
//        member.setActive(true);
//        socialLoginService.save(member);
//        session.setAttribute("member", member);
//        return "redirect:/success";
//    }

}
