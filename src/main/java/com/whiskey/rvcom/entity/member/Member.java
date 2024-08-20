package com.whiskey.rvcom.entity.member;

import com.whiskey.rvcom.entity.resource.ImageFile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "tbl_member")
@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원 식별자

    @Column(nullable = false)
    private String name;    // 이름

    @Column(nullable = false)
    private String nickname;    // 닉네임

    @Column(nullable = false, unique = true)    // 중복 방지
    private String loginId; // 로그인 아이디

    @Column(nullable = false)
    private String email;   // 이메일

    private String password;    // 비밀번호

    private String introduction;    // 자기소개

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;   // 생성 시점에 현재 시간 초기화

    private LocalDateTime deletedAt;   // 삭제 시점에 현재 시간 초기화

    @Column(nullable = false)
    private boolean isActive;   // 활성화/비활성화

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private ImageFile profileImage;    // 프로필 사진

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;    // 회원 권한

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;    // 로그인 타입
}
