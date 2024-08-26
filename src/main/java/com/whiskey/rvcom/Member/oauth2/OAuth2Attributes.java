package com.whiskey.rvcom.Member.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiskey.rvcom.Member.exception.BadRequestException;
import com.whiskey.rvcom.entity.member.LoginType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String oauthId;
    private final String nickname;
    private final String email;
    private final String picture;
    private final Provider provider;

    /**
     * OAuth2Attributes 생성자
     *
     * @param attributes 사용자 속성 맵
     * @param nameAttributeKey 사용자 이름 속성 키
     * @param oauthId OAuth ID
     * @param nickname 사용자 닉네임
     * @param email 사용자 이메일
     * @param picture 사용자 프로필 사진
     * @param provider 소셜 로그인 제공자
     */
    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String oauthId,
                            String nickname, String email, String picture, Provider provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
    }

    /**
     * 소셜 로그인 공급자에 따라 OAuth2Attributes 객체를 생성하는 정적 팩토리 메서드
     *
     * @param registrationId        소셜 로그인 공급자 ID
     * @param userNameAttributeName 사용자 이름 속성 이름
     * @param attributes            사용자 속성 맵
     * @return OAuth2Attributes 객체
     * @throws BadRequestException 지원하지 않는 소셜 로그인 공급자일 경우 예외 발생
     */
    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) throws BadRequestException {
        try {
            // 사용자 이름 속성과 사용자 속성을 로그에 기록
            log.info("userNameAttributeName = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userNameAttributeName));
            log.info("attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(attributes));
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for logging", e);
            throw new BadRequestException("Error processing JSON for logging");
        }

        // registrationId를 소문자로 변환
        String registrationIdToLower = registrationId.toLowerCase();

        // 소셜 로그인 공급자에 따라 적절한 메서드 호출
        switch (registrationIdToLower) {
            case "naver":
                return ofNaver(userNameAttributeName, attributes);

            case "google":
                return ofGoogle(userNameAttributeName, attributes);

            default:
                throw new BadRequestException("해당 소셜 로그인은 현재 지원하지 않습니다.");
        }
    }

    /**
     * 네이버 로그인 사용자의 OAuth2Attributes 객체를 생성하는 메서드
     *
     * @param userNameAttributeName 사용자 이름 속성 이름
     * @param attributes            사용자 속성 맵
     * @return OAuth2Attributes 객체
     */
    @SuppressWarnings("unchecked")
    private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        // attributes 맵에서 "response" 키의 값을 가져옴
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        // OAuth2Attributes 객체를 빌더 패턴을 사용하여 생성 및 반환
        return OAuth2Attributes.builder()
                .oauthId((String) response.get("id"))
                .nickname((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .provider(Provider.Naver)
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }

    /**
     * 구글 로그인 사용자의 OAuth2Attributes 객체를 생성하는 메서드
     *
     * @param userNameAttributeName 사용자 이름 속성 이름
     * @param attributes            사용자 속성 맵
     * @return OAuth2Attributes 객체
     */
    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        // OAuth2Attributes 객체를 빌더 패턴을 사용하여 생성 및 반환
        return OAuth2Attributes.builder()
                .oauthId((String) attributes.get(userNameAttributeName))
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .provider(Provider.Google)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * LoginType을 반환하는 메서드
     * @return LoginType
     */
    public LoginType getLoginType() {
        if (provider == Provider.Google) {
            return LoginType.GOOGLE;
        } else if (provider == Provider.Naver) {
            return LoginType.NAVER;
        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}
