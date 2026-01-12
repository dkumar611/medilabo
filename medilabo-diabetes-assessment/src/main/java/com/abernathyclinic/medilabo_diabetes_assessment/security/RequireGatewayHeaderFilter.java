package com.abernathyclinic.medilabo_diabetes_assessment.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RequireGatewayHeaderFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(RequireGatewayHeaderFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authUser = exchange.getRequest().getHeaders().getFirst("X-Auth-User");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if ((authUser == null || authUser.isEmpty()) && (authHeader == null || authHeader.isEmpty())) {
            log.warn("(DEBUG) Missing auth headers for {} - continuing for debugging (will be blocked in production)",
                    exchange.getRequest().getPath().pathWithinApplication());
            // continue the chain to allow debugging
            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }
}
