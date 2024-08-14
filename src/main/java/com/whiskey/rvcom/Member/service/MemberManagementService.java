package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberManagementService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberManagementService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public Member getMemberId(long memberId) {
        return memberRepository.getMemberById(memberId);
    }


    public boolean existsByLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public Member registerMember(String name, String nickname, String loginId, String email, String password, LoginType loginType) {
        if (existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 로그인 ID입니다.");
        }

        Member member = new Member();
        member.setName(name);
        member.setNickname(nickname);
        member.setLoginId(loginId);
        member.setEmail(email);
        member.setPassword(password);  // 인코딩된 비밀번호를 설정
        member.setLoginType(loginType != null ? loginType : LoginType.BASIC);
        member.setRole(Role.USER);
        member.setActive(true);

        return memberRepository.save(member);
    }


    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
