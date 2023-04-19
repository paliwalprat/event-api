package com.example.eventapi.repository;

import com.example.eventapi.models.VenueDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(VenueRepositoryImpl.class);
    private final WebClient webClient;
    private final String venueUri;

    @Autowired
    public VenueRepositoryImpl(WebClient webClient,@Value("${app.venue-uri}") String venueUri) {
        this.webClient = webClient;
        this.venueUri = venueUri;
    }
    @Override
    public Mono<List<VenueDO>> fetchVenues() {
        logger.info("Fetching venues from URI: {}", venueUri);
        return webClient.get().uri(venueUri).retrieve().bodyToFlux(VenueDO.class).collectList()
        .onErrorReturn(Collections.emptyList());
    }

    @Override
    public Optional<VenueDO> findVenueById(List<VenueDO> venues, String venueId) {
        logger.info("Searching for venue with ID: {}", venueId);
        return venues.stream()
                .filter(venue -> venue.getId().equals(venueId))
                .findFirst();
    }
}
