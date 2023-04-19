package com.example.eventapi.utils;

import com.example.eventapi.models.input.Event;
import com.example.eventapi.models.input.Venue;
import com.example.eventapi.models.output.EventOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventUtilsTest {
    private final EventUtils eventUtils = new EventUtils();

    @Test
    public void testIsIdValid_withValidId_shouldReturnTrue() {
        String validId = "123";
        boolean result = eventUtils.isIdValid(validId);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsIdValid_withInvalidId_shouldReturnFalse() {
        String invalidId = "abc";
        boolean result = eventUtils.isIdValid(invalidId);
        Assertions.assertFalse(result);
    }
    @Test
    void testConvertEventToEventOutput() {
        Venue venue = Venue.builder().city("Venue 1").url("test").url("test").name("Venue 1").build();

        List<Event> events = new ArrayList<>();
        events.add(Event.builder().title("Event 1").venue(venue).dateStatus("upcoming").id("1").build());
        events.add(Event.builder().title("Event 2").venue(venue).dateStatus("past").id("2").build());

        List<EventOutput> eventOutputs = eventUtils.convertEventToEventOutput(events);

        assertEquals(2, eventOutputs.size());
        assertEquals("1", eventOutputs.get(0).getId());
        assertEquals("Event 1", eventOutputs.get(0).getTitle());
        assertEquals("Venue 1", eventOutputs.get(0).getVenue().getName());
        assertEquals("upcoming", eventOutputs.get(0).getDateStatus());
        assertEquals("2", eventOutputs.get(1).getId());
        assertEquals("Event 2", eventOutputs.get(1).getTitle());
        assertEquals("Venue 1", eventOutputs.get(1).getVenue().getName());
        assertEquals("past", eventOutputs.get(1).getDateStatus());
    }
}