package com.example.eventapi.repository;

import com.example.eventapi.models.Artist;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ArtistRepository {
    Mono<List<Artist>> fetchArtists();
}
