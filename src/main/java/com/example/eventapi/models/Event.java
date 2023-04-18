package com.example.eventapi.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private String title;
    private String dateStatus;
    private String timeZone;
    private LocalDateTime startDate;
    private List<Artist> artists;
    private Venue venue;
    private Boolean hiddenFromSearch;
}