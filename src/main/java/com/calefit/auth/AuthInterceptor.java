package com.calefit.auth;

import com.calefit.auth.exception.ExpiredRefreshTokenException;
import com.calefit.auth.exception.InvalidRefreshTokenException;
import com.calefit.auth.exception.InvalidTokenException;
import com.calefit.auth.exception.NotFoundTokenException;
import com.calefit.auth.jwt.JwtHandler;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.calefit.auth.jwt.JwtConst.*;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtHandler jwtHandler;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        verifyHeader(accessToken);
        String parsedAccessToken = accessToken.split(BEARER)[1];

        try {
            Claims claims = jwtHandler.decodeJwt(parsedAccessToken);
            String email = claims.get(UNIQUE_USER_KEY, String.class);
            Member member = memberRepository.findMemberByEmail(email)
                    .orElseThrow(() -> new NoSuchElementException("no member"));

            request.setAttribute(UNIQUE_USER_KEY, member.getEmail());
        } catch(ExpiredJwtException e) {
            String refreshToken = request.getHeader("RefreshToken");
            String parsedRefreshToken = refreshToken.split(BEARER)[1];

            Claims claims = jwtHandler.decodeJwt(parsedRefreshToken);
            String email = claims.get(UNIQUE_USER_KEY, String.class);
            checkRefreshToken(email, parsedRefreshToken);
            reissueTokens(email, response);
        }

        return true;
    }

    private void reissueTokens(String email, HttpServletResponse response) {
        String reissuedAccessToken = jwtHandler.createToken(email, ACCESS_TOKEN_EXPIRATION_PERIOD);
        String reissuedRefreshToken = jwtHandler.createToken(email, REFRESH_TOKEN_EXPIRATION_PERIOD);
        response.addCookie(new Cookie("access_token", reissuedAccessToken));
        response.addCookie(new Cookie("refresh_token", reissuedRefreshToken));
        redisTemplate.opsForValue().set(email, reissuedRefreshToken);
    }

    private void checkRefreshToken(String email, String refreshToken) {
        String storedRefreshToken = redisTemplate.opsForValue().get(email);
        if (Objects.isNull(storedRefreshToken)) {
            throw new ExpiredRefreshTokenException();
        }
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }
    }

    private void verifyHeader(String authorization) {
        if (Objects.isNull(authorization) || authorization.trim().isEmpty()) {
            throw new NotFoundTokenException();
        }
        if (!authorization.startsWith(BEARER)) {
            throw new InvalidTokenException();
        }

        if (authorization.contains("undefined")) {
            throw new NotFoundTokenException();
        }
    }
}

