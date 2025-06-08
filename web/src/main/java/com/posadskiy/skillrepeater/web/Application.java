package com.posadskiy.skillrepeater.web;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@OpenAPIDefinition(
    info = @Info(
        title = "Skill Repeater Service",
        version = "0.1.0",
        description = "A service for managing and tracking skill repetition intervals to help users maintain and improve their skills through spaced repetition",
        license = @License(name = "MIT", url = "https://opensource.org/license/mit"),
        contact = @Contact(url = "https://posadskiy.com", name = "Dimitri Posadskiy", email = "support@posadskiy.com")
    ),
    servers = {
        @Server(url = "/", description = "Default Server URL")
    },
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
