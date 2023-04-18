package com.example.eventapi.repository;

import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.Event;
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
public class EventRepositoryImpl implements EventRepository{
    private final WebClient webClient;

    private final String eventUri;

    @Autowired
    public EventRepositoryImpl(WebClient webClient,@Value("${app.event-uri}") String eventUri) {
        this.webClient = webClient;
        this.eventUri = eventUri;
    }
    @Override
    public Mono<List<Event>> fetchEvents() {
        return webClient.get().uri(eventUri).retrieve().bodyToFlux(Event.class).collectList()
                .onErrorReturn(Collections.emptyList());
    }
}
