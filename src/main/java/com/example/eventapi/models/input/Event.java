package com.example.eventapi.models.input;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event extends EventBase{
    private List<Artist> artists;

}