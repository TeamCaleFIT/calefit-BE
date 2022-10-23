package com.calefit.auth.jwt;

import java.time.Duration;

public class JwtConst {

    public static final String BEARER = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRATION_PERIOD = Duration.ofMinutes(1).toMillis();
    public static final long REFRESH_TOKEN_EXPIRATION_PERIOD = Duration.ofMinutes(2).toMillis();
}
