package com.example.eventapi.service;

import com.example.eventapi.models.output.ArtistInfo;
import reactor.core.publisher.Mono;

public interface ArtistService {
    Mono<ArtistInfo> getArtistInfo(String artistId);
}
