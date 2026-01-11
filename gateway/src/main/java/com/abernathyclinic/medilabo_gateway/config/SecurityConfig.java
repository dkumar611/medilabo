package com.abernathyclinic.medilabo_gateway.config;

import com.abernathyclinic.medilabo_gateway.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails doctor = User.builder()
                .username("doctor")
                .password(encoder.encode("doctor123"))
                .roles("PHYSICIAN")
                .build();

        return new MapReactiveUserDetailsService(admin, doctor);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(form -> form
                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                            String token = JwtUtil.generateToken(authentication.getName());
                            ResponseCookie cookie = ResponseCookie.from("JWT-TOKEN", token)
                                    .httpOnly(true)
                                    .path("/")
                                    .maxAge(60 * 60)
                                    .build();
                            webFilterExchange.getExchange().getResponse().addCookie(cookie);

                            webFilterExchange.getExchange().getResponse()
                                    .setStatusCode(HttpStatus.FOUND);
                            // Use absolute redirect to the gateway host so the browser always returns to the gateway
                            webFilterExchange.getExchange().getResponse()
                                    .getHeaders()
                                    .setLocation(URI.create("http://localhost:8080/patients/view"));

                            return Mono.empty();
                        })
                        .authenticationFailureHandler((webFilterExchange, exception) -> Mono.empty())
                )
                .authorizeExchange(exchanges -> exchanges
                        // UI login + static resources
                        .pathMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // **API routes bypass security**
                        .pathMatchers("/api/**").permitAll()
                        // everything else requires JWT
                        .anyExchange().authenticated()
                )
                .build();
    }

}
