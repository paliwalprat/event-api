package com.example.eventapi.repository;

import com.example.eventapi.models.input.Event;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventRepository {
    Mono<List<Event>> fetchEvents();
    Mono<List<Event>> filterEventsByArtistId(Mono<List<Event>> eventsMono, String artistId);
}
