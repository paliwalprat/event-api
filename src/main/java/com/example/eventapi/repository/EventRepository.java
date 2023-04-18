package com.example.eventapi.repository;

import com.example.eventapi.models.Event;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventRepository {
    Mono<List<Event>> fetchEvents();
}
