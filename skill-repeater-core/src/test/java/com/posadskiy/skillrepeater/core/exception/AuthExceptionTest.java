package com.posadskiy.skillrepeater.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthExceptionTest {

    @Test
    void testAuthExceptionWithMessage() {
        String message = "Authentication failed";
        AuthException exception = new AuthException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testAuthExceptionInheritance() {
        AuthException exception = new AuthException("Test message");
        
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    void testAuthExceptionToString() {
        String message = "Test exception message";
        AuthException exception = new AuthException(message);
        
        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("AuthException"));
        assertTrue(toString.contains(message));
    }
} 