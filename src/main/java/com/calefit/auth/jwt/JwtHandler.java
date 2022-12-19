package com.calefit.auth.jwt;

import static com.calefit.auth.jwt.JwtConst.UNIQUE_USER_KEY;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtHandler {

    private final JwtProperties jwtProperties;

    @Autowired
    private JwtHandler(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(String email, long expirationPeriod) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .claim(UNIQUE_USER_KEY, email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationPeriod)))
                .signWith(createSecretKey())
                .compact();
    }

    private SecretKey createSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public Claims decodeJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
