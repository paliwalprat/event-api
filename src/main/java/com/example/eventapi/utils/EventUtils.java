package com.example.eventapi.utils;

public class EventUtils {
    public static boolean isIdValid(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
