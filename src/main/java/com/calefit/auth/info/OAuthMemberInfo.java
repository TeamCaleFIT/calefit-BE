package com.calefit.auth.info;

import com.calefit.auth.ProviderType;
import com.calefit.member.entity.Member;

import java.util.Map;

public abstract class OAuthMemberInfo {

    protected Map<String, Object> attributes;

    public OAuthMemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getMemberId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getProfileUrl();

    public abstract ProviderType getProviderType();

    public Member toMember() {
        return new Member(getMemberId(), getEmail(), getName(), getProfileUrl() ,getProviderType());
    }
}
