package com.example.eventapi.repository;

import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Repository
public class VenueRepositoryImpl implements VenueRepository {

    private final WebClient webClient;
    private final String venueUri;

    @Autowired
    public VenueRepositoryImpl(WebClient webClient,@Value("${app.event-uri}") String venueUri) {
        this.webClient = webClient;
        this.venueUri = venueUri;
    }
    @Override
    public Mono<List<Venue>> fetchVenues() {
        return webClient.get().uri(venueUri).retrieve().bodyToFlux(Venue.class).collectList()
        .onErrorReturn(Collections.emptyList());
    }
}
