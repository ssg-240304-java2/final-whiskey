package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String loginId);
    Member findByLoginIdAndLoginType(String loginId, LoginType loginType);
    boolean existsByLoginId(String loginId);
    Member getMemberById(long memberId);
    Page<Member> findAllByLoginType(LoginType loginType, Pageable pageable);
    Page<Member> findAll(Pageable pageable);
}
