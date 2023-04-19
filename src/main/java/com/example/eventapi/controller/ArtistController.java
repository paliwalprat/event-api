package com.example.eventapi.controller;

import com.example.eventapi.models.ArtistInfo;
import com.example.eventapi.service.ArtistService;
import com.example.eventapi.utils.EventUtils;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Import(ExceptionHandlerController.class)
@RequestMapping("/api/v1/artists")
public class ArtistController {
    private final ArtistService artistService;

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping(path="/{artistId}",produces= MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ArtistInfo>> getArtistInfo(@PathVariable String artistId) {
        if (StringUtils.isBlank(artistId) || !EventUtils.isIdValid(artistId)) {
            logger.info("Invalid artist ID provided: {}", artistId);
            return Mono.just(ResponseEntity.badRequest().build());
        }
        logger.info("Retrieving artist information for ID: {}", artistId);
        return artistService.getArtistInfo(artistId)
                .doOnSuccess(info -> logger.info("Successfully retrieved artist information for ID: {}", artistId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(ex -> logger.error("Error occurred while retrieving artist information: {}", ex.getMessage(), ex));
    }
}
