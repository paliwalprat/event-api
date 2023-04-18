package com.example.eventapi.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventUtilsTest {
    @Test
    public void testIsIdValid_withValidId_shouldReturnTrue() {
        String validId = "123";
        boolean result = EventUtils.isIdValid(validId);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsIdValid_withInvalidId_shouldReturnFalse() {
        String invalidId = "abc";
        boolean result = EventUtils.isIdValid(invalidId);
        Assertions.assertFalse(result);
    }
}