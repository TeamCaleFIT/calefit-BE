package com.calefit.auth.info.impl;

import com.calefit.auth.domain.provider.ProviderType;
import com.calefit.auth.info.OAuthMemberInfo;

import java.util.HashMap;
import java.util.Map;

public class KakaoMemberInfo extends OAuthMemberInfo {

    private ProviderType providerType = ProviderType.valueOf("KAKAO");

    public KakaoMemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getMemberId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }

        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        Map kakao_account = (HashMap) attributes.get("kakao_account");
        return (String) kakao_account.get("email");
    }

    @Override
    public String getProfileUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }

        return (String) properties.get("profile_image");
    }

    @Override
    public ProviderType getProviderType() {
        return providerType;
    }
}

