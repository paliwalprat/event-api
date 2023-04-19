package com.example.eventapi.controller;

import com.example.eventapi.models.input.Artist;
import com.example.eventapi.models.output.ArtistInfo;
import com.example.eventapi.models.input.Event;
import com.example.eventapi.models.input.Venue;
import com.example.eventapi.models.output.EventOutput;
import com.example.eventapi.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ArtistControllerTest {
    @Mock
    private ArtistService artistService;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(new ArtistController(artistService)).build();
    }

    @Test
    public void testGetArtistInfo() {
        Artist artist = Artist.builder().id("123").rank(1).name("test").imgSrc("test").build();
        Venue venue = Venue.builder().city("testCity").url("test").url("test").name("test").build();
        EventOutput event = EventOutput.builder().title("test")
                .venue(venue).dateStatus("present").id("12").build();
        ArtistInfo artistInfo = ArtistInfo.builder().artist(artist).events(Collections.singletonList(event)).build();
        when(artistService.getArtistInfo(anyString())).thenReturn(Mono.just(artistInfo));

        webTestClient.get().uri("/api/v1/artists/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtistInfo.class)
                .isEqualTo(artistInfo);
    }
    @Test
    public void testGetArtistInfoNotFound() {
        when(artistService.getArtistInfo(anyString())).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/v1/artists/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetArtistInfoInvalidId() {
        webTestClient.get().uri("/api/v1/artists/abc")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testGetArtistInfoServiceError() {
        when(artistService.getArtistInfo(anyString())).thenReturn(Mono.error(new RuntimeException()));

        webTestClient.get().uri("/api/v1/artists/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}