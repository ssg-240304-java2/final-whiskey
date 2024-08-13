package com.whiskey.rvcom.Member.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final Collection<? extends GrantedAuthority> authorities;

    // 수정된 생성자 - ROLE_USER로 기본 권한 설정
    public CustomOAuth2User(Map<String, Object> attributes, String nameAttributeKey) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // 기본 권한을 ROLE_USER로 설정
    }

    // 필요 시 추가적인 생성자 - 권한을 외부에서 주입받을 수 있도록
    public CustomOAuth2User(Map<String, Object> attributes, String nameAttributeKey, Collection<? extends GrantedAuthority> authorities) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.authorities = authorities; // 외부에서 권한을 주입받아 설정
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return (String) attributes.get(nameAttributeKey);
    }
}
