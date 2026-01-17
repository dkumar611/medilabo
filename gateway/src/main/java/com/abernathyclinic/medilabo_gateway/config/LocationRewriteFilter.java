package com.abernathyclinic.medilabo_gateway.config;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class LocationRewriteFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            if (!headers.containsKey(HttpHeaders.LOCATION)) return;

            URI loc = headers.getLocation();
            if (loc == null) return;

            // Build a new Location using the original request's scheme and host (gateway)
            String origScheme = exchange.getRequest().getURI().getScheme();
            String origHost = exchange.getRequest().getHeaders().getFirst(HttpHeaders.HOST);
            if (origHost == null || origHost.isEmpty()) return;

            StringBuilder sb = new StringBuilder();
            sb.append(origScheme != null ? origScheme : "http");
            sb.append("://");
            sb.append(origHost);

            String pathAndQuery = (loc.getRawPath() != null ? loc.getRawPath() : "");
            sb.append(pathAndQuery);
            if (loc.getRawQuery() != null && !loc.getRawQuery().isEmpty()) {
                sb.append('?').append(loc.getRawQuery());
            }

            try {
                URI newLoc = URI.create(sb.toString());
                headers.setLocation(newLoc);
            } catch (Exception ignored) {
                // If anything fails, don't rewrite
            }
        }));
    }

    @Override
    public int getOrder() {
        // After NettyWriteResponse filter
        return Ordered.LOWEST_PRECEDENCE;
    }
}

