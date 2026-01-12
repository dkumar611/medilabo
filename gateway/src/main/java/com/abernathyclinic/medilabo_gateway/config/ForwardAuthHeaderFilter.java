package com.abernathyclinic.medilabo_gateway.config;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.Authentication;

@Component
public class ForwardAuthHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    Authentication auth = securityContext.getAuthentication();
                    if (auth != null && auth.isAuthenticated()) {
                        // Add X-Auth-User header with username
                        ServerWebExchange mutated = exchange.mutate()
                                .request(r -> r.header("X-Auth-User", auth.getName()))
                                .build();

                        // If JWT cookie exists, forward Authorization: Bearer <token> and Cookie header
                        var cookie = exchange.getRequest().getCookies().getFirst("JWT-TOKEN");
                        if (cookie != null) {
                            String token = cookie.getValue();
                            mutated = mutated.mutate()
                                    .request(r -> r.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                            .header(HttpHeaders.COOKIE, "JWT-TOKEN=" + token))
                                    .build();
                        }

                        return chain.filter(mutated);
                    }

                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        // Run after authentication filter so a security context is available
        return Ordered.LOWEST_PRECEDENCE;
    }
}
