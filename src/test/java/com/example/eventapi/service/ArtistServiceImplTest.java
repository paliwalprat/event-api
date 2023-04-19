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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class ArtistServiceImplTest {

    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private VenueRepository venueRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        artistService = new ArtistServiceImpl(artistRepository, eventRepository, venueRepository);
    }

    @Test
    public void testGetArtistInfo() {
        String artistId = "1";
        Artist artist = Artist.builder().id(artistId).rank(1).name("test").imgSrc("test").build();
        Venue venue = Venue.builder().city("testCity").url("test").url("test").name("test").id("1").build();
        Event event = Event.builder().artists(Collections.singletonList(artist)).title("test")
                .venue(venue).dateStatus("present").id("12").build();

        List<Event> events = Collections.singletonList(event);
        List<Venue> venues = Collections.singletonList(venue);

        when(artistRepository.fetchArtists()).thenReturn(Mono.just(Collections.singletonList(artist)));
        when(eventRepository.fetchEvents()).thenReturn(Mono.just(events));
        when(venueRepository.fetchVenues()).thenReturn(Mono.just(venues));

        ArtistInfo expectedArtistInfo = new ArtistInfo(artist, events);

        StepVerifier.create(artistService.getArtistInfo(artistId))
                .expectNext(expectedArtistInfo)
                .verifyComplete();
    }

    @Test
    void givenInvalidArtistId_whenGetArtistInfo_thenThrowsArtistNotFoundException() {
        String artistId = "1";

        List<Artist> artists = Collections.emptyList();

        when(artistRepository.fetchArtists()).thenReturn(Mono.just(artists));

        Mono<ArtistInfo> result = artistService.getArtistInfo(artistId);

        StepVerifier.create(result)
                .expectError(ArtistNotFoundException.class)
                .verify();
    }
        @Test
    void givenNullArtistId_whenGetArtistInfo_thenThrowsIllegalArgumentException() {
        Mono<ArtistInfo> result = artistService.getArtistInfo(null);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void getArtistInfo_withWebClientResponseException_shouldThrowCustomWebClientException() {
        String artistId = "123";

        when(artistRepository.fetchArtists()).thenReturn(Mono.error(new CustomWebClientException("Error occurred while fetching data: Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR)));
        when(eventRepository.fetchEvents()).thenReturn(Mono.just(new ArrayList<>()));
        when(venueRepository.fetchVenues()).thenReturn(Mono.just(new ArrayList<>()));

        Mono<ArtistInfo> artistInfoMono = artistService.getArtistInfo(artistId);

        StepVerifier.create(artistInfoMono)
                .expectError(CustomWebClientException.class)
                .verify();
    }
}