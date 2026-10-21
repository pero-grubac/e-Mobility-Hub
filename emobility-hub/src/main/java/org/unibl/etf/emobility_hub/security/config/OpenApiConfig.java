package org.unibl.etf.emobility_hub.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Swagger UI at /swagger-ui.html: log in through POST /auth/login, then paste
// the returned token into "Authorize".
@Configuration
public class OpenApiConfig {
    private static final String BEARER = "bearerAuth";

    @Bean
    OpenAPI eMobilityHubOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("eMobilityHub API")
                        .description("Rental platform for electric cars, bicycles and scooters.")
                        .version("1.0")
                        .license(new License().name("MIT")))
                .components(new Components().addSecuritySchemes(BEARER, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER));
    }
}
