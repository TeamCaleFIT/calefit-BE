package com.calefit.auth.jwt;

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

    public String createToken(String memberId, long expirationPeriod) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(expirationPeriod)))
                .signWith(createSecretKey())
                .compact();
    }

    private SecretKey createSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /* todo: parseClaimsJws()에서 발생하는 예외 케이스들
        - UnsupportedJwtException : jwt가 예상하는 형식과 다른 형식이거나 구성
        - MalformedJwtException : 잘못된 jwt 구조
        - ExpiredJwtException : JWT의 유효기간이 초과 -> 이 핸들러에서 리프레시 토큰을 확인해봐야 하는데 음.. 어떻게?
        - SignatureException : JWT의 서명실패(변조 데이터)
        위 예외 처리하도록 핸들러 추가하기 */
    public Claims decodeJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
