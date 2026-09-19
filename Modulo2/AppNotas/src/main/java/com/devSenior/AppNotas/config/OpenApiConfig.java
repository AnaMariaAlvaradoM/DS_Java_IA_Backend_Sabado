package com.devSenior.AppNotas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfig — le dice a Swagger que la API usa JWT.
 * Esto es lo que hace aparecer el boton "Authorize" en la interfaz.
 */
@Configuration
@OpenAPIDefinition(
        security = { @SecurityRequirement(name = "bearerAuth") }
)
@SecurityScheme(
        name = "bearerAuth",            // nombre interno del esquema
        type = SecuritySchemeType.HTTP, // autenticacion HTTP
        scheme = "bearer",              // tipo Bearer
        bearerFormat = "JWT"            // el formato es JWT
)
public class OpenApiConfig {
    // No necesita codigo: las anotaciones hacen todo el trabajo
}