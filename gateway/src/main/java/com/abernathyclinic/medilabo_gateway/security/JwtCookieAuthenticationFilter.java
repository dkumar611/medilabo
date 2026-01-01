package com.abernathyclinic.medilabo_gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtCookieAuthenticationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var cookie = exchange.getRequest().getCookies().getFirst("JWT-TOKEN");
        if (cookie == null) {
            return chain.filter(exchange);
        }

        try {
            Claims claims = JwtUtil.validateToken(cookie.getValue());
            String username = claims.getSubject();

            var auth = new UsernamePasswordAuthenticationToken(username, null, java.util.List.of());

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (Exception e) {
            return chain.filter(exchange);
        }
    }
}
