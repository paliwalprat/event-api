package com.example.eventapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventDO extends EventBase {
    private List<ArtistDO> artists;

}