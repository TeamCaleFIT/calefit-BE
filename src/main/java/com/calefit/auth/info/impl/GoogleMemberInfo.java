package com.calefit.auth.info.impl;

import com.calefit.auth.info.OAuthMemberInfo;

import java.util.Map;

public class GoogleMemberInfo extends OAuthMemberInfo {

//    private ProviderType providerType = ProviderType.valueOf("GOOGLE");

    public GoogleMemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getMemberId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProfileUrl() {
        return (String) attributes.get("picture");
    }

//    @Override
//    public ProviderType getProviderType() {
//        return providerType;
//    }
}
