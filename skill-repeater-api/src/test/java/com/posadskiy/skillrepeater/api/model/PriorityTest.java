package com.posadskiy.skillrepeater.api.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriorityTest {

    @Test
    void testPriorityValues() {
        assertEquals(1, Priority.LOW.getValue());
        assertEquals(2, Priority.MEDIUM.getValue());
        assertEquals(3, Priority.HIGH.getValue());
        assertEquals(4, Priority.CRITICAL.getValue());
    }

    @Test
    void testPriorityDisplayNames() {
        assertEquals("Low", Priority.LOW.getDisplayName());
        assertEquals("Medium", Priority.MEDIUM.getDisplayName());
        assertEquals("High", Priority.HIGH.getDisplayName());
        assertEquals("Critical", Priority.CRITICAL.getDisplayName());
    }

    @Test
    void testFromValue() {
        assertEquals(Priority.LOW, Priority.fromValue(1));
        assertEquals(Priority.MEDIUM, Priority.fromValue(2));
        assertEquals(Priority.HIGH, Priority.fromValue(3));
        assertEquals(Priority.CRITICAL, Priority.fromValue(4));
    }

    @Test
    void testFromValueInvalid() {
        // Should default to MEDIUM for invalid values
        assertEquals(Priority.MEDIUM, Priority.fromValue(0));
        assertEquals(Priority.MEDIUM, Priority.fromValue(5));
    }

    @Test
    void testFromString() {
        assertEquals(Priority.LOW, Priority.fromString("LOW"));
        assertEquals(Priority.MEDIUM, Priority.fromString("MEDIUM"));
        assertEquals(Priority.HIGH, Priority.fromString("HIGH"));
        assertEquals(Priority.CRITICAL, Priority.fromString("CRITICAL"));
    }

    @Test
    void testFromStringCaseInsensitive() {
        assertEquals(Priority.LOW, Priority.fromString("low"));
        assertEquals(Priority.MEDIUM, Priority.fromString("medium"));
        assertEquals(Priority.HIGH, Priority.fromString("high"));
        assertEquals(Priority.CRITICAL, Priority.fromString("critical"));
    }

    @Test
    void testFromStringInvalid() {
        // Should default to MEDIUM for invalid values
        assertEquals(Priority.MEDIUM, Priority.fromString("INVALID"));
        assertEquals(Priority.MEDIUM, Priority.fromString(""));
    }
} 