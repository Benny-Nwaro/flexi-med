package com.example.flexiMed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * This class defines a bean to configure the OpenAPI documentation for the application,
 * providing metadata such as the API title, version, and description.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures and returns an OpenAPI bean.
     * This bean is used to customize the OpenAPI documentation generated for the application.
     * It sets the API title, version, and description.
     *
     * @return An instance of {@link OpenAPI} with custom metadata.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FlexiMed API")
                        .version("1.0")
                        .description("Flexi Medical Dispatch API"));
    }
}