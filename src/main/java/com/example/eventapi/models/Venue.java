package com.example.eventapi.models;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
    private String name;
    private String url;
    private String city;
    private String id;
}