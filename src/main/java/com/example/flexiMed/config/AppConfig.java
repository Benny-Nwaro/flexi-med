package com.example.flexiMed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-wide beans.
 * This class configures and provides beans that are used throughout the application,
 * such as a {@link RestTemplate} instance.
 */
@Configuration
public class AppConfig {

    /**
     * Configures and returns a {@link RestTemplate} bean.
     * {@link RestTemplate} is used for making HTTP requests to external services.
     *
     * @return A new instance of {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}