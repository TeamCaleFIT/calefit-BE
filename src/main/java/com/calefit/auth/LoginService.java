package com.calefit.auth;

import com.calefit.auth.domain.OAuthProperties;
import com.calefit.auth.domain.provider.OAuthProvider;
import com.calefit.auth.info.OAuthMemberInfo;
import com.calefit.auth.info.OAuthMemberInfoFactory;
import com.calefit.auth.jwt.JwtHandler;
import com.calefit.auth.jwt.Token;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import lombok.RequiredArgsConstructor;
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

import static com.calefit.auth.jwt.JwtConst.*;

@Service
@RequiredArgsConstructor
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
        //레디스에 리프레시 토큰을 저장하고 리프레시 토큰 만료기간에 맞춰 레디스에서도 삭제되도록 함.
        redisTemplate.expire(oAuthMemberInfo.getMemberId(), Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_PERIOD));

        return new Token(
                jwtHandler.createToken(oAuthMemberInfo.getMemberId(), ACCESS_TOKEN_EXPIRATION_PERIOD),
                refreshToken
        );
    }

    private OAuthAccessToken getOauthAccessToken(String code, OAuthProvider provider) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> header = new HashMap<>();
        header.put(HttpHeaders.ACCEPT, "application/json");
        headers.setAll(header);

        MultiValueMap<String, String> requestPayloads = createRequestPayloads(code, provider);
        HttpEntity<?> request = new HttpEntity<>(requestPayloads, headers);

        ResponseEntity<?> response = new RestTemplate().postForEntity(
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
}
