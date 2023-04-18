package com.example.eventapi.config;

import com.example.eventapi.exception.CustomWebClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final String baseUrl;
    private final WebClient.Builder webClientBuilder;

    public WebClientConfig(@Value("${app.base-url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.baseUrl = baseUrl;
        this.webClientBuilder = webClientBuilder;
    }
    @Bean
    public WebClient webClient() throws CustomWebClientException {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new CustomWebClientException("Base URL is null or empty",null);
        }
        try {
            return webClientBuilder.baseUrl(baseUrl).build();
        } catch (Exception e) {
            throw new CustomWebClientException("Error creating WebClient: " + e.getMessage(),null);
        }
    }
}