package com.calefit.auth.config;

import com.calefit.auth.domain.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@RequiredArgsConstructor
@PropertySource(value = "classpath:application-oauth.yml", factory = YmlLoadFactory.class)
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthConfig {

    private final OAuthProperties properties;
}
