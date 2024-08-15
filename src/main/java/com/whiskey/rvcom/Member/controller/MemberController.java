package com.whiskey.rvcom.Member.controller;

import com.whiskey.rvcom.Member.service.MemberManagementService;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {

    private final SocialLoginService socialLoginService;
    private final MemberManagementService memberManagementService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberController(SocialLoginService socialLoginService, MemberManagementService memberManagementService, PasswordEncoder passwordEncoder) {
        this.socialLoginService = socialLoginService;
        this.memberManagementService = memberManagementService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "mainPage"; // index.html 반환
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 반환
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Member member = memberManagementService.findByLoginId(loginId);
        if (member == null) {
            model.addAttribute("error", "존재하지 않는 회원입니다.");
            return "login";
        }


        if (passwordEncoder.matches(password, member.getPassword())) {
            session.setAttribute("member", member);
            session.setAttribute("isAuthenticated", true);
            session.setAttribute("userRole", member.getRole().toString());

            return "redirect:/mainPage";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "login";
        }
    }


    @GetMapping("/mainPage")
    public String mainPage(HttpSession session, Model model) {
        model.addAttribute("isAuthenticated", session.getAttribute("isAuthenticated"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "mainPage";
    }


    @GetMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @PostMapping("/checkLoginId")
    public ResponseEntity<Map<String, Boolean>> checkLoginId(@RequestParam String loginId) {
        boolean exists = memberManagementService.existsByLoginId(loginId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register_basic")
    public String registerBasic(@RequestParam Map<String, String> allParams, Model model) {
        String loginId = allParams.get("loginId");
        String password = allParams.get("password");
        String name = allParams.get("name");
        String email = allParams.get("email");
        String nickname = allParams.get("nickname");

        if (memberManagementService.existsByLoginId(loginId)) {
            model.addAttribute("error", "이미 존재하는 이메일입니다. 다른 이메일을 사용해주세요.");
            return "register_basic"; // 회원가입 페이지로 다시 돌아감
        }

        String encodedPassword = passwordEncoder.encode(password); // 한 번만 인코딩

        try {
            memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, LoginType.BASIC);
        } catch (Exception e) {
            model.addAttribute("error", "이미 존재하는 로그인 ID입니다. 다른 ID를 사용해주세요.");
            return "register_basic";
        }

        return "redirect:/login";
    }

    @PostMapping("/register_social")
    public String registerSocial(@RequestParam String nickname, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        String loginTypeStr = (String) session.getAttribute("loginType");
        Map<String, Object> userAttributes = (Map<String, Object>) session.getAttribute("userAttributes");

        if (loginId == null || loginTypeStr == null || userAttributes == null) {
            model.addAttribute("error", "세션 정보가 없습니다. 다시 로그인해 주세요.");
            return "redirect:/login";
        }

        LoginType loginType = LoginType.valueOf(loginTypeStr);

        try {
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
            member.setRole(Role.USER); // 기본 역할을 설정
            member.setActive(true);
            member.setPassword(null);

            socialLoginService.save(member);

            session.setAttribute("isAuthenticated", true);
            session.setAttribute("userRole", member.getRole().toString());
            session.setAttribute("member", member);


            return "redirect:/mainPage";
        } catch (Exception ex) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "register_social";
        }
    }


    @GetMapping("/register_basic")
    public String showBasicRegisterForm(Model model) {
        model.addAttribute("isSocialLogin", false);
        return "register_basic";
    }

    @GetMapping("/register_social")
    public String showSocialRegisterForm(HttpSession session, Model model) {
        String loginType = (String) session.getAttribute("loginType");
        Map<String, Object> userAttributes = (Map<String, Object>) session.getAttribute("userAttributes");

        if (loginType != null && userAttributes != null) {
            model.addAttribute("userAttributes", userAttributes);
        } else {
            model.addAttribute("userAttributes", new HashMap<String, Object>()); // 빈 맵 전달
        }
        model.addAttribute("isSocialLogin", true);
        return "register_social";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session completely
        return "redirect:/login";
    }
}
