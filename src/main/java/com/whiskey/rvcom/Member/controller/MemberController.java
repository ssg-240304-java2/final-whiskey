package com.whiskey.rvcom.Member.controller;

import com.whiskey.rvcom.Member.emailauth.EmailService;
import com.whiskey.rvcom.Member.service.MemberManagementService;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import com.whiskey.rvcom.Member.service.VerificationService;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class MemberController {

    private final ReviewService reviewService;
    private final SocialLoginService socialLoginService;
    private final MemberManagementService memberManagementService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Autowired
    public MemberController(ReviewService reviewService, SocialLoginService socialLoginService,
                            MemberManagementService memberManagementService, PasswordEncoder passwordEncoder,
                            RestTemplate restTemplate, EmailService emailService, VerificationService verificationService) {
        this.reviewService = reviewService;
        this.socialLoginService = socialLoginService;
        this.memberManagementService = memberManagementService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @GetMapping("/")
    public String home() {
        return "mainPage";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        if (Boolean.TRUE.equals(isAuthenticated) && userRole != null) {
            return "redirect:/mainPage";
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession session, Model model) {
        Member member = memberManagementService.findByLoginId(loginId);
        if (member == null) {
            model.addAttribute("error", "존재하지 않는 회원입니다.");
            return "login";
        }

        if (!member.isActive()) {
            model.addAttribute("alertMessage", "해당 계정은 비활성화되어 있습니다. 관리자에게 문의하세요.");
            return "login";
        }

        if (passwordEncoder.matches(password, member.getPassword())) {
            setSessionAttributes(session, member);
            return "redirect:/mainPage";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "login";
        }
    }


    @GetMapping("/mainPage")
    public String mainPage(HttpSession session, Model model) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("userRole", userRole);

        return "mainPage";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        Boolean isSocialLogin = member.getLoginType() != LoginType.BASIC;
        model.addAttribute("member", member);
        model.addAttribute("isSocialLogin", isSocialLogin);

        return "mypage";
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
        String verificationCode = allParams.get("emailVerificationCode");

        if (memberManagementService.existsByLoginId(loginId)) {
            model.addAttribute("error", "이미 존재하는 로그인 ID입니다. 다른 ID를 사용해주세요.");
            return "register_basic";
        }

        if (!verificationService.verifyCode(email, verificationCode)) {
            model.addAttribute("error", "인증 코드가 올바르지 않습니다.");
            return "register_basic";
        }

        String encodedPassword = passwordEncoder.encode(password);

        try {
            memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, LoginType.BASIC);
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "register_basic";
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/api/redis/save")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        try {
            String verificationCode = verificationService.generateAndSaveVerificationCode(email);
            emailService.sendVerificationCode(email, verificationCode);
            log.info("Generated and sent verification code to email: {}", email);
            return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            log.error("Error during sending verification code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 코드 전송 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
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
            member.setLoginType(loginType); // LoginType 설정
            member.setRole(Role.USER);
            member.setActive(true);
            member.setPassword(null);

            // 사용자에게 환영 메시지를 설정
            member.setIntroduction("안녕하세요 " + nickname + " 만나서 반갑습니다");

            socialLoginService.save(member);
            setSessionAttributes(session, member);

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
            model.addAttribute("userAttributes", new HashMap<>());
        }
        model.addAttribute("isSocialLogin", true);
        return "register_social";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/mypage";
    }

    @PostMapping("/updateProfileBasic")
    public String updateProfileBasic(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        log.info("Received request to update basic profile with params: {}", params);

        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        // 전달받은 파라미터를 멤버에 적용하기 전에 로깅
        log.info("Updating basic member with new parameters - Name: {}, Nickname: {}, Introduction: {}",
                params.get("name"), params.get("nickname"), params.get("introduction"));

        member.setName(params.get("name"));
        member.setNickname(params.get("nickname"));
        member.setEmail(params.get("email"));

        String password = params.get("password");
        if (password != null && !password.isEmpty()) {
            member.setPassword(passwordEncoder.encode(password));
        }

        // "자기소개" 필드 업데이트
        member.setIntroduction(params.get("introduction"));

        // 변경 사항을 저장하는 코드
        memberManagementService.updateMember(member);

        log.info("Successfully updated member: {}", member);

        session.setAttribute("member", member);
        model.addAttribute("member", member);

        return "redirect:/mypage";
    }

    @PostMapping("/updateProfileSocial")
    public String updateProfileSocial(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        log.info("Received request to update social profile with params: {}", params);

        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        // 전달받은 파라미터를 멤버에 적용하기 전에 로깅
        log.info("Updating social member with new parameters - Nickname: {}, Introduction: {}",
                params.get("nickname"), params.get("introduction"));

        member.setNickname(params.get("nickname"));
        member.setIntroduction(params.get("introduction"));

        // 변경 사항을 저장하는 코드
        socialLoginService.updateMember(member);

        log.info("Successfully updated social member: {}", member);

        session.setAttribute("member", member);
        model.addAttribute("member", member);

        return "redirect:/mypage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/mainPage";
    }

    @PostMapping("/deactivateAccount")
    public String deactivateAccount(HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        // 회원의 로그인 타입에 따라 적절한 서비스를 호출
        if (member.getLoginType() == LoginType.BASIC) {
            // 일반 로그인 회원의 비활성화 처리
            memberManagementService.deactivateMember(member);
        } else {
            // 소셜 로그인 회원의 비활성화 처리
            socialLoginService.deactivateMember(member);
        }

        // 세션 무효화
        session.invalidate();

        log.info("Member {} has deactivated their account.", member.getLoginId());

        return "redirect:/mainPage";
    }

    private void setSessionAttributes(HttpSession session, Member member) {
        session.setAttribute("member", member);
        session.setAttribute("isAuthenticated", true);
        session.setAttribute("userRole", member.getRole().toString());
        session.setAttribute("loginType", member.getLoginType().toString());
        log.info("Session loginType set to: {}", member.getLoginType().toString());
    }
}
