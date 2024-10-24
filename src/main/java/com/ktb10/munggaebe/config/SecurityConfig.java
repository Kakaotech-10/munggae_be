package com.ktb10.munggaebe.config;

import com.ktb10.munggaebe.auth.filter.JwtAuthenticationFilter;
import com.ktb10.munggaebe.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/v1/auth/login/oauth2/callback/kakao").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/comments/**", "/api/v1/posts/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api/v1/comments/**", "/api/v1/posts/**").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/comments/**", "/api/v1/posts/**").hasRole("MANAGER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
