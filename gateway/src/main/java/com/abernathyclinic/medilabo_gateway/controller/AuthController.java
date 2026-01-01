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

        ResponseCookie cookie = ResponseCookie.from("MEDILABO_TOKEN", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();

        response.addCookie(cookie);

        response.setStatusCode(org.springframework.http.HttpStatus.FOUND);
        response.getHeaders().setLocation(
                java.net.URI.create("/patients/view")
        );

        return response.setComplete();
    }
}
