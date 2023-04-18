package com.example.eventapi.repository;

import com.example.eventapi.models.Venue;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VenueRepository {
    Mono<List<Venue>> fetchVenues();
}
