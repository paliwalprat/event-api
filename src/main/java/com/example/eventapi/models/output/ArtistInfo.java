package com.example.eventapi.models.output;

import com.example.eventapi.models.input.Artist;
import jdk.jfr.Event;
import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtistInfo {
    private Artist artist;
    private List<EventOutput> events;
}
