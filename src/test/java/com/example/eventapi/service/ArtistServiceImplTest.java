package com.example.eventapi.service;

import com.example.eventapi.exception.ArtistNotFoundException;
import com.example.eventapi.exception.CustomWebClientException;
import com.example.eventapi.models.input.Artist;
import com.example.eventapi.models.output.ArtistInfo;
import com.example.eventapi.models.input.Event;
import com.example.eventapi.models.input.Venue;
import com.example.eventapi.models.output.EventOutput;
import com.example.eventapi.repository.ArtistRepository;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.VenueRepository;
import com.example.eventapi.utils.EventUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ArtistServiceImplTest {

    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private EventUtils eventUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        artistService = new ArtistServiceImpl(artistRepository, eventRepository, venueRepository,eventUtils);
    }

    @Test
    public void testGetArtistInfo() {
        String artistId = "1";
        Artist artist = Artist.builder().id(artistId).rank(1).name("Test Artist").imgSrc("test").build();
        Venue venue = Venue.builder().city("testCity").url("test").name("Test Venue").id("1").build();
        Event event = Event.builder().artists(Collections.singletonList(artist)).title("Test Event")
                .venue(venue).dateStatus("present").id("12").build();
        EventOutput eventOutput = EventOutput.builder().title("Test Event")
                .venue(venue).dateStatus("present").id("12").build();
        List<Event> events = Collections.singletonList(event);
        List<Venue> venues = Collections.singletonList(venue);

        when(artistRepository.fetchArtists()).thenReturn(Mono.just(Arrays.asList(artist)));
        when(artistRepository.findArtistById(anyList(), eq(artistId))).thenReturn(Optional.of(artist));
        when(eventRepository.fetchEvents()).thenReturn(Mono.just(events));
        when(eventRepository.filterEventsByArtistId(any(Mono.class), eq(artistId))).thenReturn(Mono.just(events));
        when(venueRepository.fetchVenues()).thenReturn(Mono.just(venues));
        when(venueRepository.findVenueById(anyList(), anyString())).thenAnswer(invocation -> {
            String venueId = invocation.getArgument(1);
            return venues.stream().filter(v -> v.getId().equals(venueId)).findFirst();
        });
        when(eventUtils.convertEventToEventOutput(events)).thenReturn(List.of(EventOutput.builder()
                .id(event.getId())
                .title(event.getTitle())
                .venue(venue)
                .dateStatus(event.getDateStatus())
                .build()));

        ArtistInfo expectedArtistInfo = new ArtistInfo(artist, Collections.singletonList(eventOutput));

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