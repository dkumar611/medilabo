package com.abernathyclinic.medilabo_gateway.controller;

import com.abernathyclinic.medilabo_gateway.security.JwtUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {

    @PostMapping("/login")
    public Mono<Void> login(Authentication authentication,
                            org.springframework.http.server.reactive.ServerHttpResponse response) {

        String jwt = JwtUtil.generateToken(authentication.getName());

        // Use the same cookie name as SecurityConfig and JwtCookieAuthenticationFilter
        ResponseCookie cookie = ResponseCookie.from("JWT-TOKEN", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();

        response.addCookie(cookie);

        response.setStatusCode(org.springframework.http.HttpStatus.FOUND);
        // Redirect back to the gateway host so the browser always navigates via the gateway
        response.getHeaders().setLocation(
                java.net.URI.create("http://localhost:8080/patients/view")
        );

        return response.setComplete();
    }
}
