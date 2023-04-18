package com.example.eventapi.models;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtistInfo {
    private Artist artist;
    private List<Event> events;
}
