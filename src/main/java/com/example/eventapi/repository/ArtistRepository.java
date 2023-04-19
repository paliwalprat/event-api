package com.example.eventapi.repository;

import com.example.eventapi.models.input.Artist;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {
    Mono<List<Artist>> fetchArtists();
    Optional<Artist> findArtistById(List<Artist> artists, String artistId);
}
