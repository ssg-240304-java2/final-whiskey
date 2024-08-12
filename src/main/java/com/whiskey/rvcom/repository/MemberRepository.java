package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}
