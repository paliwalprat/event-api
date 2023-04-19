package com.example.eventapi.config;

import com.example.eventapi.exception.CustomWebClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

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
    void testWebClientWithNullBaseUrl() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClientConfig webClientConfig = new WebClientConfig(null, webClientBuilder);

        assertThrows(CustomWebClientException.class, () -> {
            webClientConfig.webClient();
        });
    }

    @Test
    void testWebClientWithEmptyBaseUrl() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClientConfig webClientConfig = new WebClientConfig("", webClientBuilder);

        assertThrows(CustomWebClientException.class, () -> {
            webClientConfig.webClient();
        });
    }
}