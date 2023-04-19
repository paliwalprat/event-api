package com.example.eventapi.utils;

import com.example.eventapi.models.EventDO;
import com.example.eventapi.models.EventOutput;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventUtils {
    private static final Logger logger = LoggerFactory.getLogger(EventUtils.class);
    public static boolean isIdValid(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException ex) {
            logger.error("Error parsing ID: {}", ex.getMessage());
            return false;
        }
    }

    public List<EventOutput> convertEventToEventOutput(List<EventDO> events) {
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
        logger.info("Converted {} events to EventOutput", eventOutputs.size());
        return eventOutputs;
    }
}
