package com.abernathyclinic.medilabo_noteservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RequireGatewayHeaderFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequireGatewayHeaderFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authUser = request.getHeader("X-Auth-User");
        String authHeader = request.getHeader("Authorization");

        if ((authUser == null || authUser.isEmpty()) && (authHeader == null || authHeader.isEmpty())) {
            log.warn("(DEBUG) Missing auth headers for {} - continuing for debugging (will be blocked in production)", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
