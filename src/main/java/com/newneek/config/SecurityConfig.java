package com.newneek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // CORS 설정 활성화
                .and()
                .csrf().disable() // 필요에 따라 CSRF 비활성화
                .authorizeRequests()
                .requestMatchers("/api/**", "/uploads/**").permitAll() // "/api/**" 패턴에 대한 요청은 인증 없이 허용
                .anyRequest().authenticated(); // 그 외의 요청은 인증을 요구

        return http.build();
    }
}
