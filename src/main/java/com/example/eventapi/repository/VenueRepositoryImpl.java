package com.example.eventapi.repository;

import com.example.eventapi.models.input.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class VenueRepositoryImpl implements VenueRepository {

    private final WebClient webClient;
    private final String venueUri;

    @Autowired
    public VenueRepositoryImpl(WebClient webClient,@Value("${app.venue-uri}") String venueUri) {
        this.webClient = webClient;
        this.venueUri = venueUri;
    }
    @Override
    public Mono<List<Venue>> fetchVenues() {
        return webClient.get().uri(venueUri).retrieve().bodyToFlux(Venue.class).collectList()
        .onErrorReturn(Collections.emptyList());
    }

    @Override
    public Optional<Venue> findVenueById(List<Venue> venues, String venueId) {
        return venues.stream()
                .filter(venue -> venue.getId().equals(venueId))
                .findFirst();
    }
}
