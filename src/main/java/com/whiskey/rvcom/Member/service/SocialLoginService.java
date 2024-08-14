package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginService {

    private final MemberRepository memberRepository;

    @Autowired
    public SocialLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByLoginIdAndLoginType(String loginId, LoginType type) {
        return memberRepository.findByLoginIdAndLoginType(loginId, type);
    }

    public Member getMemberId(long memberId) {
        return memberRepository.getMemberById(memberId);
    }

    public void handleSocialLogin(HttpSession session, String loginId, String name, String email, String loginType) {
        if (loginType == null) {
            throw new IllegalArgumentException("Login type cannot be null");
        }

        LoginType type = LoginType.valueOf(loginType.toUpperCase());

        Member member = findMemberByLoginIdAndLoginType(loginId, type);

        if (member != null) {
            session.setAttribute("member", member);
        } else {
            session.setAttribute("userAttributes", Map.of("name", name, "email", email, "loginType", loginType, "loginId", loginId));
        }
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
