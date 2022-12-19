package com.calefit.auth.info;

import com.calefit.auth.domain.provider.ProviderType;
import com.calefit.auth.info.impl.GoogleMemberInfo;
import com.calefit.auth.info.impl.KakaoMemberInfo;
import com.calefit.auth.info.impl.NaverMemberInfo;

import java.util.Map;

public class OAuthMemberInfoFactory {
    public static OAuthMemberInfo createOAuthMemberInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE:
                return new GoogleMemberInfo(attributes);
            case NAVER:
                return new NaverMemberInfo(attributes);
            case KAKAO:
                return new KakaoMemberInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
