package com.ktb10.munggaebe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${custom.client.base-url}")
    private String clientBaseUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", clientBaseUrl)
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
