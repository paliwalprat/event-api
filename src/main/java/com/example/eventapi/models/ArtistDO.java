package com.example.eventapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDO {
    private String id;
    private String name;
    private String imgSrc;
    private String url;
    private Integer rank;
}
