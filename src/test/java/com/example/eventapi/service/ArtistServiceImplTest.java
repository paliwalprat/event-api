package com.example.eventapi.service;

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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
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
    public void testGetArtistInfo_artistNotFound() {
        String artistId = "1";
        Artist artist = Artist.builder().id("123").rank(1).name("test").imgSrc("test").build();

        when(artistRepository.fetchArtists()).thenReturn(Mono.just(Collections.singletonList(artist)));

        StepVerifier.create(artistService.getArtistInfo(artistId))
                .expectErrorMatches(throwable -> throwable instanceof CustomWebClientException &&
                        throwable.getMessage().equals("Artist not found for artistId " + artistId))
                .verify();
    }

    @Test
    public void testGetArtistInfo_errorFetchingArtistInfo() {
        String artistId = "1";

        when(artistRepository.fetchArtists()).thenReturn(Mono.error(new RuntimeException("Error fetching artist info from API")));

        StepVerifier.create(artistService.getArtistInfo(artistId))
                .expectErrorMatches(throwable -> throwable instanceof CustomWebClientException &&
                        ((CustomWebClientException) throwable).getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR &&
                        throwable.getMessage().equals("Error fetching artist info from API"))
                .verify();
    }
}