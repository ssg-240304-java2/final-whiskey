package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
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

        // 자동으로 자기소개를 설정
        member.setIntroduction("안녕하세요 " + nickname + " 만나서 반갑습니다");

        return memberRepository.save(member);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void updateMember(Member member) {
        System.out.println("회원 업데이트 트랜잭션 시작");

        // 영속성 컨텍스트에 의해 관리되고 있는지 확인
        if (memberRepository.existsById(member.getId())) {
            System.out.println("엔티티가 영속성 컨텍스트에 의해 관리되고 있습니다.");
        } else {
            System.out.println("엔티티가 영속성 컨텍스트에 의해 관리되고 있지 않습니다.");
        }

        memberRepository.save(member);

        System.out.println("회원 정보 업데이트 완료");
    }

    public Page<Member> getMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Transactional
    public void deactivateMember(Member member) {
        member.setActive(false);
        member.setDeletedAt(LocalDateTime.now());
        memberRepository.save(member);
    }

}
