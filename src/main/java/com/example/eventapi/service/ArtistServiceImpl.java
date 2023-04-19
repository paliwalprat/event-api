package com.example.eventapi.service;

import com.example.eventapi.exception.ArtistNotFoundException;
import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.ArtistDO;
import com.example.eventapi.models.ArtistInfo;
import com.example.eventapi.models.EventDO;
import com.example.eventapi.models.VenueDO;
import com.example.eventapi.repository.ArtistRepository;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.VenueRepository;
import com.example.eventapi.utils.EventUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    private static final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventUtils eventUtils;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository, EventRepository eventRepository,
                             VenueRepository venueRepository, EventUtils eventUtils) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventUtils = eventUtils;
    }

    /**
     * Retrieves artist information, including their events and venues, for the specified artist ID.
     *
     * @param artistId The ID of the artist to retrieve information for.
     * @return A Mono object that emits a single value of type ArtistInfo.
     * @throws IllegalArgumentException If the artistId is null or empty.
     * @throws CustomWebClientException If an error occurs while fetching the data.
     */
    @Override
    public Mono<ArtistInfo> getArtistInfo(String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Artist ID must not be null or empty"));
        }

        Mono<List<ArtistDO>> artistsMono = artistRepository.fetchArtists();
        Mono<List<EventDO>> eventsMono = eventRepository.fetchEvents();
        Mono<List<VenueDO>> venuesMono = venueRepository.fetchVenues();

        return artistsMono
                .flatMap(artists -> {
                    ArtistDO artist = artistRepository.findArtistById(artists, artistId)
                            .orElseThrow(() -> new ArtistNotFoundException("Artist not found for artistId " + artistId));
                    Mono<List<EventDO>> filteredEventsMono = eventRepository.filterEventsByArtistId(eventsMono, artistId);
                    return enrichEventsWithVenues(venuesMono, filteredEventsMono, artist);
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    String errorMessage = "Error occurred while fetching data: " + ex.getMessage();
                    logger.error(errorMessage);
                    return Mono.error(new CustomWebClientException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .doOnSuccess(artistInfo -> logger.info("Successfully retrieved artist information for artistId {}", artistId));

    }

    private Mono<ArtistInfo> enrichEventsWithVenues(Mono<List<VenueDO>> venuesMono, Mono<List<EventDO>> eventsMono, ArtistDO artist) {
        return Mono.zip(venuesMono, eventsMono)
                .map(tuple -> {
                    List<VenueDO> venues = tuple.getT1();
                    List<EventDO> events = tuple.getT2();
                    for (EventDO event : events) {
                        Optional<VenueDO> venueOpt = venueRepository.findVenueById(venues, event.getVenue().getId());
                        venueOpt.ifPresent(event::setVenue);
                    }
                    return ArtistInfo.builder().artist(artist).events(eventUtils.convertEventToEventOutput(events)).build();
                });
    }

}
