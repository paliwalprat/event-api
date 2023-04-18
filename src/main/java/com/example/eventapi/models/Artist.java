package com.example.eventapi.models;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    private String id;
    private String name;
    private String imgSrc;
    private String url;
    private Integer rank;
}
