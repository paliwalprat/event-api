package com.example.eventapi.utils;

import com.example.eventapi.models.input.Event;
import com.example.eventapi.models.output.EventOutput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventUtils {
    public static boolean isIdValid(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public List<EventOutput> convertEventToEventOutput(List<Event> events) {
        List<EventOutput> eventOutputs = new ArrayList<>();
        for (var event : events) {
            eventOutputs.add(EventOutput.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .venue(event.getVenue())
                    .dateStatus(event.getDateStatus())
                    .timeZone(event.getTimeZone())
                    .build());
        }
        return eventOutputs;
    }
}
