package com.calefit.auth.domain;

import com.calefit.auth.domain.provider.OAuthProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2.client")
public class OAuthProperties {

    private final Map<String, OAuthProvider> provider;

    public OAuthProvider getProvider(String name) {
        return provider.get(name);
    }
}
