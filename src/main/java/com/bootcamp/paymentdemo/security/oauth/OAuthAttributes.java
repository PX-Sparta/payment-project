package com.bootcamp.paymentdemo.security.oauth;

import com.bootcamp.paymentdemo.domain.customer.entity.Customer;
import com.bootcamp.paymentdemo.domain.customer.enums.Rank;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        // 카카오 이메일 동의 안 했을 경우 가짜 이메일 생성 방어 로직 추가
        String email = (String) response.get("email");
        if (email == null || email.isEmpty()) {
            email = attributes.get("id") + "@social.sparta.com";
        }

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email(email)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // [수정 핵심] 신규 유저일 경우 엔티티 생성 시 필수값 모두 주입
    public Customer toEntity() {
        return Customer.builder()
                .email(email)
                .name(name == null ? "소셜유저" : name)
                .password("OAUTH_USER_NO_PASSWORD")
                .phoneNumber("010-0000-0000") // DB 제약조건 방어
                .currentPoint(0L)             // DB 제약조건 방어
                .rank(Rank.NORMAL)            // DB 제약조건 방어
                .build();
    }
}