package com.example.eventapi.controller;

import com.example.eventapi.models.Artist;
import com.example.eventapi.models.ArtistInfo;
import com.example.eventapi.service.ArtistService;
import com.example.eventapi.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Import(ExceptionHandlerController.class)
@RequestMapping("/api/v1/artists")
public class ArtistController {
    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping(path="/{artistId}",produces= MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ArtistInfo>> getArtistInfo(@PathVariable String artistId) {
        if (artistId ==null || !EventUtils.isIdValid(artistId)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return artistService.getArtistInfo(artistId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
