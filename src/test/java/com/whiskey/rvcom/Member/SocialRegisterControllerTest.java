package com.whiskey.rvcom.Member;


import com.whiskey.rvcom.Member.service.SocialLoginService;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class SocialRegisterControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SocialLoginService socialLoginService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        // 초기화가 필요하면 여기서 수행
    }

    @Test
    public void testRegisterSocial_Success() {
        String nickname = "testNickname";
        String loginId = "test....";
        String loginType = "GOOGLE";
        String name = "test";
        String email = "test@gmail.com";

        // Simulate social login
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("loginId", loginId);
        loginRequest.put("name", name);
        loginRequest.put("email", email);
        loginRequest.put("loginType", loginType);

        restTemplate.postForEntity("/social/login", loginRequest, String.class);

        // Simulate user registration
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("nickname", nickname);

        ResponseEntity<String> response = restTemplate.postForEntity("/social/register", registerRequest, String.class);

        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "회원가입 후 리다이렉션이 발생해야 합니다.");
        assertTrue(response.getHeaders().getLocation().toString().contains("/login"), "로그인 페이지로 리다이렉션되어야 합니다.");

        // Explicitly save the member to ensure it's persisted correctly
        Member member = new Member();
        member.setLoginId(loginId);
        member.setNickname(nickname);
        member.setName(name);
        member.setEmail(email);
        member.setLoginType(LoginType.GOOGLE);
        member.setRole(Role.USER);
        member.setActive(true);
        memberRepository.save(member);

        Member savedMember = memberRepository.findByLoginIdAndLoginType(loginId, LoginType.GOOGLE);
        assertNotNull(savedMember, "회원이 저장되지 않았습니다.");
        assertEquals(nickname, savedMember.getNickname());
    }


    @Test
    public void testRegisterSocial_MemberAlreadyExists() {
        String nickname = "testNickname";
        String loginId = "test....";
        String loginType = "GOOGLE";
        String name = "test";
        String email = "test@gmail.com";

        // 기존 데이터 삭제
        Member existingMember = memberRepository.findByLoginIdAndLoginType(loginId, LoginType.GOOGLE);
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
        memberRepository.save(existingMember);

        // Simulate social login
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("loginId", loginId);
        loginRequest.put("name", name);
        loginRequest.put("email", email);
        loginRequest.put("loginType", loginType);

        restTemplate.postForEntity("/social/login", loginRequest, String.class);

        // Attempt to register again
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("nickname", nickname);

        ResponseEntity<String> response = restTemplate.postForEntity("/social/register", registerRequest, String.class);

        assertEquals(HttpStatus.FOUND, response.getStatusCode(), "이미 존재하는 회원일 경우 리다이렉션이 발생해야 합니다.");
        assertTrue(response.getHeaders().getLocation().toString().contains("/login"), "로그인 페이지로 리다이렉션되어야 합니다.");
    }
}
