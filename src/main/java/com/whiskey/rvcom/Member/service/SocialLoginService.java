package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
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
            // 계정 비활성화 여부 확인
            if (!member.isActive()) {
                throw new IllegalArgumentException("해당 계정은 비활성화되어 있습니다.");
            }
            session.setAttribute("member", member);
        } else {
            session.setAttribute("userAttributes", Map.of("name", name, "email", email, "loginType", loginType, "loginId", loginId));
        }
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

    @Transactional
    public void deactivateMember(Member member) {
        member.setActive(false);
        member.setDeletedAt(LocalDateTime.now());
        memberRepository.save(member);
    }

    public Page<Member> getMembers(Pageable pageable, LoginType loginType) {
        return memberRepository.findAllByLoginType(loginType, pageable);
    }


}
