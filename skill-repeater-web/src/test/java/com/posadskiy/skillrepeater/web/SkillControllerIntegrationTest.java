package com.posadskiy.skillrepeater.web;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class SkillControllerIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testAccessingSkillsEndpointWithoutAuthenticationReturnsUnauthorized() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/api/skills").accept(APPLICATION_JSON));
        });

        assertEquals(UNAUTHORIZED, e.getStatus());
    }

    @Test
    void testSwaggerUiIsAccessibleWithoutAuthentication() {
        HttpResponse<String> response = client.toBlocking().exchange(
            HttpRequest.GET("/swagger-ui/index.html").accept(APPLICATION_JSON),
            String.class
        );

        assertEquals(OK, response.getStatus());
        assertNotNull(response.body());
        assertTrue(response.body().contains("swagger"));
    }

    @Test
    void testOpenApiSpecificationIsAccessibleWithoutAuthentication() {
        HttpResponse<String> response = client.toBlocking().exchange(
            HttpRequest.GET("/swagger/skill-repeater-service-0.2.0.yml").accept(APPLICATION_JSON),
            String.class
        );

        assertEquals(OK, response.getStatus());
        assertNotNull(response.body());
        assertTrue(response.body().contains("openapi"));
    }

    @Test
    void testHealthEndpointIsAccessible() {
        HttpResponse<String> response = client.toBlocking().exchange(
            HttpRequest.GET("/health").accept(APPLICATION_JSON),
            String.class
        );

        assertEquals(OK, response.getStatus());
        assertNotNull(response.body());
    }

    @Test
    void testMetricsEndpointIsAccessible() {
        HttpResponse<String> response = client.toBlocking().exchange(
            HttpRequest.GET("/prometheus").accept(APPLICATION_JSON),
            String.class
        );

        assertEquals(OK, response.getStatus());
        assertNotNull(response.body());
        assertTrue(response.body().contains("jvm_"));
    }
} 