package com.posadskiy.skillrepeater.web;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class ApplicationTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testApplicationIsRunning() {
        assertTrue(application.isRunning());
    }

    @Test
    void testApplicationHasEnvironment() {
        assertNotNull(application.getEnvironment());
    }

    @Test
    void testApplicationHasApplicationContext() {
        assertNotNull(application.getApplicationContext());
    }

    @Test
    void testApplicationCanBeStarted() {
        // Given
        EmbeddedApplication<?> app = application;

        // When & Then
        assertTrue(app.isRunning());
        assertDoesNotThrow(() -> {
            // Application should already be running
            assertTrue(app.isRunning());
        });
    }

    @Test
    void testApplicationHasConfiguration() {
        assertNotNull(application.getApplicationContext().getEnvironment());
    }
}
