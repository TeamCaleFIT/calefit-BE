package com.calefit.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OAuthAccessToken {

    @JsonProperty("access_token")
    private String accessToken;
}
