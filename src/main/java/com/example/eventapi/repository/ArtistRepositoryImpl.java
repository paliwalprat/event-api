package com.example.eventapi.repository;

import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.ArtistDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public class ArtistRepositoryImpl implements ArtistRepository {
    private static final Logger logger = LoggerFactory.getLogger(ArtistRepositoryImpl.class);
    private final WebClient webClient;
    private final String artistUri;

    @Autowired
    public ArtistRepositoryImpl(WebClient webClient,@Value("${app.artist-uri}") String artistUri) {
        this.webClient = webClient;
        this.artistUri = artistUri;
    }

    @Override
    public Mono<List<ArtistDO>> fetchArtists() {
        return webClient.get().uri(artistUri)
                .retrieve().bodyToFlux(ArtistDO.class).collectList()
                .doOnSuccess(artists -> logger.info("Fetched {} artists from {}", artists.size(), artistUri))
                .onErrorResume(WebClientException.class,
                        ex -> {
                    logger.error("Error occurred while fetching artists from {}: {}", artistUri, ex.getMessage(), ex);
                            return Mono.error(new CustomWebClientException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                        });
    }

    @Override
    public Optional<ArtistDO> findArtistById(List<ArtistDO> artists, String artistId) {
        return artists.stream()
                .filter(artist -> artist.getId().equals(artistId))
                .findFirst();
    }
}
