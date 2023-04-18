package com.example.eventapi.config;

import com.example.eventapi.exception.CustomWebClientException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "app.base-url=http://localhost:8080")
@ActiveProfiles("test")
public class WebClientConfigTest {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Test
    public void webClient_returnsWebClientInstance() throws CustomWebClientException {
        WebClient webClient = new WebClientConfig(baseUrl, webClientBuilder).webClient();
        assertThat(webClient).isNotNull();
    }

    @Test
    public void webClient_throwsExceptionWhenBaseUrlIsNull() {
        assertThatThrownBy(() -> new WebClientConfig(null, webClientBuilder).webClient())
                .isInstanceOf(CustomWebClientException.class)
                .hasMessageContaining("Base URL is null or empty");
    }

    @Test
    public void webClient_throwsExceptionWhenBaseUrlIsEmpty() {
        assertThatThrownBy(() -> new WebClientConfig("", webClientBuilder).webClient())
                .isInstanceOf(CustomWebClientException.class)
                .hasMessageContaining("Base URL is null or empty");
    }
}