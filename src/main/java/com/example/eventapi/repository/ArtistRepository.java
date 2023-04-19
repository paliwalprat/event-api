package com.example.eventapi.repository;

import com.example.eventapi.models.ArtistDO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {
    Mono<List<ArtistDO>> fetchArtists();
    Optional<ArtistDO> findArtistById(List<ArtistDO> artists, String artistId);
}
