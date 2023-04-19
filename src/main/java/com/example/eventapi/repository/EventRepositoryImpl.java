package com.example.eventapi.repository;

import com.example.eventapi.models.EventDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventRepositoryImpl implements EventRepository{
    private static final Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);
    private final WebClient webClient;

    private final String eventUri;

    @Autowired
    public EventRepositoryImpl(WebClient webClient,@Value("${app.event-uri}") String eventUri) {
        this.webClient = webClient;
        this.eventUri = eventUri;
    }
    @Override
    public Mono<List<EventDO>> fetchEvents() {
        return webClient.get().uri(eventUri).retrieve().bodyToFlux(EventDO.class).collectList()
                .doOnSuccess(events -> logger.info("Fetched {} events from {}", events.size(), eventUri))
                .onErrorReturn(Collections.emptyList());
    }
    @Override
    public  Mono<List<EventDO>> filterEventsByArtistId(Mono<List<EventDO>> eventsMono, String artistId) {
        return eventsMono.map(events -> events.stream()
                .filter(event -> event.getArtists().stream().anyMatch(a -> a.getId().equals(artistId)))
                .collect(Collectors.toList()));
    }
}
