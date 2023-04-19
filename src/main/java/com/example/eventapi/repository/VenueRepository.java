package com.example.eventapi.repository;

import com.example.eventapi.models.VenueDO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface VenueRepository {
    Mono<List<VenueDO>> fetchVenues();
    Optional<VenueDO> findVenueById(List<VenueDO> venues, String venueId);
}
