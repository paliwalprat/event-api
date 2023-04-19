package com.example.eventapi.service;

import com.example.eventapi.exception.ArtistNotFoundException;
import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.input.Artist;
import com.example.eventapi.models.output.ArtistInfo;
import com.example.eventapi.models.input.Event;
import com.example.eventapi.models.input.Venue;
import com.example.eventapi.repository.ArtistRepository;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.VenueRepository;
import com.example.eventapi.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

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

    @Override
    public Mono<ArtistInfo> getArtistInfo(String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Artist ID must not be null or empty"));
        }

        Mono<List<Artist>> artistsMono = artistRepository.fetchArtists();
        Mono<List<Event>> eventsMono = eventRepository.fetchEvents();
        Mono<List<Venue>> venuesMono = venueRepository.fetchVenues();

        return artistsMono
                .flatMap(artists -> {
                    Artist artist = artistRepository.findArtistById(artists, artistId)
                            .orElseThrow(() -> new ArtistNotFoundException("Artist not found for artistId " + artistId));
                    Mono<List<Event>> filteredEventsMono = eventRepository.filterEventsByArtistId(eventsMono, artistId);
                    return enrichEventsWithVenues(venuesMono, filteredEventsMono, artist);
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    String errorMessage = "Error occurred while fetching data: " + ex.getMessage();
                    return Mono.error(new CustomWebClientException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    private Mono<ArtistInfo> enrichEventsWithVenues(Mono<List<Venue>> venuesMono, Mono<List<Event>> eventsMono, Artist artist) {
        return Mono.zip(venuesMono, eventsMono)
                .map(tuple -> {
                    List<Venue> venues = tuple.getT1();
                    List<Event> events = tuple.getT2();
                    for (Event event : events) {
                        Optional<Venue> venueOpt = venueRepository.findVenueById(venues, event.getVenue().getId());
                        venueOpt.ifPresent(event::setVenue);
                    }
                    return ArtistInfo.builder().artist(artist).events(eventUtils.convertEventToEventOutput(events)).build();
                });
    }

}
