package com.posadskiy.skillrepeater.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessageTest {

    @Test
    void testErrorMessageCreation() {
        ErrorMessage errorMessage = new ErrorMessage(false, "Test error message");
        
        assertEquals(false, errorMessage.status());
        assertEquals("Test error message", errorMessage.message());
    }

    @Test
    void testErrorMessageEqualsAndHashCode() {
        ErrorMessage errorMessage1 = new ErrorMessage(false, "Test error message");
        ErrorMessage errorMessage2 = new ErrorMessage(false, "Test error message");
        ErrorMessage errorMessage3 = new ErrorMessage(true, "Test error message");
        
        assertEquals(errorMessage1, errorMessage2);
        assertEquals(errorMessage1.hashCode(), errorMessage2.hashCode());
        assertNotEquals(errorMessage1, errorMessage3);
    }

    @Test
    void testErrorMessageToString() {
        ErrorMessage errorMessage = new ErrorMessage(false, "Test error message");
        String toString = errorMessage.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("false"));
        assertTrue(toString.contains("Test error message"));
    }

    @Test
    void testErrorMessageWithNullMessage() {
        ErrorMessage errorMessage = new ErrorMessage(true, null);
        
        assertEquals(true, errorMessage.status());
        assertNull(errorMessage.message());
    }

    @Test
    void testErrorMessageWithEmptyMessage() {
        ErrorMessage errorMessage = new ErrorMessage(false, "");
        
        assertEquals(false, errorMessage.status());
        assertEquals("", errorMessage.message());
    }
} 