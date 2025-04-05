package com.example.flexiMed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration class for Spring MVC settings.
 * This class configures resource handlers to serve static resources from the uploads directory.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Configures resource handlers for serving static resources.
     * This method adds a resource handler to serve files from the "uploads" directory.
     * It dynamically resolves the absolute path of the uploads directory.
     *
     * @param registry The resource handler registry.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Dynamically resolve the uploads directory
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}