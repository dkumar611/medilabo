package com.abernathyclinic.medilabo_diabetes_assessment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(gatewayBaseUrl)
                .build();
    }
}


