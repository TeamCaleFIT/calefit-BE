package com.calefit.auth.jwt;

import java.time.Duration;

public class JwtConst {

    public static final String BEARER = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRATION_PERIOD = Duration.ofMinutes(30).toSeconds();
    public static final long REFRESH_TOKEN_EXPIRATION_PERIOD = Duration.ofMinutes(24 * 60 * 7).toSeconds();
}
