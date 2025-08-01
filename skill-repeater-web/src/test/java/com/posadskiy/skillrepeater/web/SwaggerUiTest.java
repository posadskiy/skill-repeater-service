package com.posadskiy.skillrepeater.web;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class SwaggerUiTest {

    @Test
    void shouldServeOpenApiSpecification(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertDoesNotThrow(() -> client.exchange("/swagger/swagger.yml"));
    }

    @Test
    void shouldGenerateSwaggerUiResources(ResourceLoader resourceLoader) {
        assertTrue(resourceLoader
                .getResource("META-INF/swagger/views/swagger-ui/index.html")
                .isPresent());
    }

    @Test
    void shouldServeSwaggerUi(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertDoesNotThrow(() -> client.exchange("/swagger-ui/index.html"));
    }
}
