package com.ktb10.munggaebe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.auth.filter.JwtAuthenticationFilter;
import com.ktb10.munggaebe.auth.filter.JwtExceptionFilter;
import com.ktb10.munggaebe.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final CorsFilter corsFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico", "/api/v1/auth/login/oauth2/callback/kakao", "/healthcheck", "/api/v1/mentions/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/comments/**", "/api/v1/posts/**", "/api/v1/members/**").permitAll()
                        .requestMatchers("/api/v1/notifications/subscribe").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);

        return http.build();
    }
}
