package com.calefit.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                    .antMatchers("/**").permitAll()
                    .anyRequest().permitAll()// 가입 및 인증 주소는 누구나 접근가능
                    .and()
                .csrf()
                    .ignoringAntMatchers("/**")
                    .ignoringAntMatchers("/h2-console/**")
                    .and()
                    .headers()
                        .frameOptions().sameOrigin()
                        .and();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
