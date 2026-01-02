package com.abernathyclinic.medilabo_diabetes_assessment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        // Allow all requests and disable CSRF
        return http
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
}
