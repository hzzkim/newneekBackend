package com.newneek.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2UserInfo {

    private OAuth2User oAuth2User;

    // 동적으로 객체 설정
    public void setOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }
}