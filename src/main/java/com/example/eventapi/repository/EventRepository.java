package com.example.eventapi.repository;

import com.example.eventapi.models.EventDO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventRepository {
    Mono<List<EventDO>> fetchEvents();
    Mono<List<EventDO>> filterEventsByArtistId(Mono<List<EventDO>> eventsMono, String artistId);
}
