package com.calefit.auth.domain.provider;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OAuthProvider {

    // client
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    // path
    private String accessTokenPath;
    private String resourcePath;
}
