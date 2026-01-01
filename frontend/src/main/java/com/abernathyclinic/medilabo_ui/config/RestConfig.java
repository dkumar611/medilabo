package com.abernathyclinic.medilabo_ui.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            // Prefer JSON responses from backend
            request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs != null) {
                HttpServletRequest incomingRequest = attrs.getRequest();

                if (incomingRequest.getCookies() != null &&
                        !request.getHeaders().containsKey("Cookie")) {

                    for (Cookie cookie : incomingRequest.getCookies()) {
                        if ("JWT-TOKEN".equals(cookie.getName())) {
                            request.getHeaders()
                                    .add("Cookie", "JWT-TOKEN=" + cookie.getValue());
                        }
                    }
                }
            }

            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
