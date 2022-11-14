package com.calefit.auth;

import com.calefit.auth.domain.OAuthProperties;
import com.calefit.auth.domain.provider.OAuthProvider;
import com.calefit.auth.exception.InvalidTokenException;
import com.calefit.auth.exception.NotFoundTokenException;
import com.calefit.auth.info.OAuthMemberInfo;
import com.calefit.auth.info.OAuthMemberInfoFactory;
import com.calefit.auth.jwt.JwtHandler;
import com.calefit.auth.jwt.Token;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.calefit.auth.jwt.JwtConst.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final JwtHandler jwtHandler;
    private final OAuthProperties properties;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    public Token createToken(String code, String providerType) {
        OAuthProvider provider = properties.getProvider(providerType);
        OAuthAccessToken oauthAccessToken = getOauthAccessToken(code, provider);
        Map<String, Object> memberInfo = getMemberInfo(oauthAccessToken, provider);
        OAuthMemberInfo oAuthMemberInfo = OAuthMemberInfoFactory.createOAuthMemberInfo(
                ProviderType.valueOf(providerType.toUpperCase()),
                memberInfo
        );
        saveMember(oAuthMemberInfo);

        String refreshToken = jwtHandler.createToken(oAuthMemberInfo.getMemberId(), REFRESH_TOKEN_EXPIRATION_PERIOD);
        redisTemplate.opsForValue().set(oAuthMemberInfo.getMemberId(), refreshToken);
        redisTemplate.expire(oAuthMemberInfo.getMemberId(), Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_PERIOD));

        return new Token(
                jwtHandler.createToken(oAuthMemberInfo.getMemberId(), ACCESS_TOKEN_EXPIRATION_PERIOD),
                refreshToken
        );
    }

    public void deleteStoredToken(String refreshToken) {
        verifyHeader(refreshToken);
        String parsedRefreshToken = refreshToken.split("Bearer ")[1];
        Claims claims = jwtHandler.decodeJwt(parsedRefreshToken);
        String memberId = claims.get("memberId", String.class);
        redisTemplate.delete(memberId);
    }

    private OAuthAccessToken getOauthAccessToken(String code, OAuthProvider provider) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> header = new HashMap<>();
//        header.put(HttpHeaders.ACCEPT, "application/json;charset=utf-8");
        header.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        headers.setAll(header);
        MultiValueMap<String, String> requestPayloads = createRequestPayloads(code, provider);
        log.info("code: {}", code);
        log.info("client_id: {}", provider.getClientId());
        log.info("client_secret: {}", provider.getClientSecret());
        log.info("redirect_uri: {}", provider.getRedirectUri());
        log.info("access token path: {}", provider.getAccessTokenPath());
        HttpEntity<?> request = new HttpEntity<>(requestPayloads, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.postForEntity(
                provider.getAccessTokenPath(), request, OAuthAccessToken.class);

        return (OAuthAccessToken) response.getBody();
    }

    private MultiValueMap<String, String> createRequestPayloads(String code, OAuthProvider provider) {
        MultiValueMap<String, String> requestPayloads = new LinkedMultiValueMap<>();
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("client_id", provider.getClientId());
        requestPayload.put("client_secret", provider.getClientSecret());
        requestPayload.put("code", code);
        requestPayload.put("grant_type", "authorization_code");
        requestPayload.put("redirect_uri", provider.getRedirectUri());
        requestPayloads.setAll(requestPayload);
        return requestPayloads;
    }

    private Map<String, Object> getMemberInfo(OAuthAccessToken OAuthAccessToken, OAuthProvider provider) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER + OAuthAccessToken.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<Map<String, Object>> response = new RestTemplate().exchange(
                provider.getResourcePath(),
                HttpMethod.GET,
                request,
                PARAMETERIZED_RESPONSE_TYPE
        );
        return response.getBody();
    }

    private void saveMember(OAuthMemberInfo memberInfo) {
        Member findMember = memberRepository.findMemberByMemberId(memberInfo.getMemberId())
                .map(member -> member.update(memberInfo.getEmail(), memberInfo.getName(), memberInfo.getProfileUrl()))
                .orElseGet(memberInfo::toMember);

        memberRepository.save(findMember);
    }

    private void verifyHeader(String refreshToken) {
        if (Objects.isNull(refreshToken) || refreshToken.trim().isEmpty()) {
            throw new NotFoundTokenException();
        }
        if (!refreshToken.startsWith(BEARER)) {
            throw new InvalidTokenException();
        }

        if (refreshToken.contains("undefined")) {
            throw new NotFoundTokenException();
        }
    }
}
