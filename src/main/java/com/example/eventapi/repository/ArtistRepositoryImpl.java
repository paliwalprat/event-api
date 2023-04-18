package com.example.eventapi.repository;

import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.Artist;
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
public class ArtistRepositoryImpl implements ArtistRepository {

    private final WebClient webClient;
    private final String artistUri;

    @Autowired
    public ArtistRepositoryImpl(WebClient webClient,@Value("${app.artist-uri}") String artistUri) {
        this.webClient = webClient;
        this.artistUri = artistUri;
    }

    @Override
    public Mono<List<Artist>> fetchArtists() {
        return webClient.get().uri(artistUri)
                .retrieve().bodyToFlux(Artist.class).collectList()
                .onErrorResume(WebClientException.class,
                        ex -> Mono.error(new CustomWebClientException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}
