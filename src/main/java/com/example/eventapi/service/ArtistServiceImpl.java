package com.example.eventapi.service;

import com.example.eventapi.exception.ArtistNotFoundException;
import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.Artist;
import com.example.eventapi.models.ArtistInfo;
import com.example.eventapi.models.Event;
import com.example.eventapi.models.Venue;
import com.example.eventapi.repository.ArtistRepository;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository, EventRepository eventRepository, VenueRepository venueRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
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
                    Artist artist = findArtistById(artists, artistId)
                            .orElseThrow(() -> new ArtistNotFoundException("Artist not found for artistId " + artistId));
                    Mono<List<Event>> filteredEventsMono = filterEventsByArtistId(eventsMono, artistId);
                    return enrichEventsWithVenues(venuesMono, filteredEventsMono, artist);
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    String errorMessage = "Error occurred while fetching data: " + ex.getMessage();
                    return Mono.error(new CustomWebClientException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    private Mono<List<Event>> filterEventsByArtistId(Mono<List<Event>> eventsMono, String artistId) {
        return eventsMono.map(events -> events.stream()
                .filter(event -> event.getArtists().stream().anyMatch(a -> a.getId().equals(artistId)))
                .collect(Collectors.toList()));
    }

    private Mono<ArtistInfo> enrichEventsWithVenues(Mono<List<Venue>> venuesMono, Mono<List<Event>> eventsMono, Artist artist) {
        return Mono.zip(venuesMono, eventsMono)
                .map(tuple -> {
                    List<Venue> venues = tuple.getT1();
                    List<Event> events = tuple.getT2();
                    for (Event event : events) {
                        Optional<Venue> venueOpt = findVenueById(venues, event.getVenue().getId());
                        venueOpt.ifPresent(event::setVenue);
                    }
                    return ArtistInfo.builder().artist(artist).events(events).build();
                });
    }

    private Optional<Venue> findVenueById(List<Venue> venues, String venueId) {
        return venues.stream()
                .filter(venue -> venue.getId().equals(venueId))
                .findFirst();
    }

    private Optional<Artist> findArtistById(List<Artist> artists, String artistId) {
        return artists.stream()
                .filter(artist -> artist.getId().equals(artistId))
                .findFirst();
    }

}
