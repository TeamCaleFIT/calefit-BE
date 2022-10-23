package com.calefit.auth.info.impl;

import com.calefit.auth.info.OAuthMemberInfo;

import java.util.Map;

public class KakaoMemberInfo extends OAuthMemberInfo {

//    private ProviderType providerType = ProviderType.valueOf("KAKAO");

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

        return (String) properties.get("profile_nickname"); //name은 선택 제공정보, profile_nickname은 필수 제공 정보
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("account_email");
    }

    @Override
    public String getProfileUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }

        return (String) properties.get("profile_image");
    }

//    @Override
//    public ProviderType getProviderType() {
//        return providerType;
//    }
}

