package com.alammoah.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        // ✅ FIXED: Use a wildcard pattern instead of hardcoded Vercel preview URLs.
                        // Each Vercel deployment gets a unique URL like alam-mo-ah-v2-abc123.vercel.app
                        // so hardcoding them caused CORS failures on every new deployment.
                        .allowedOriginPatterns(
                                "https://*.vercel.app",
                                "http://localhost:*"  // keeps local dev working too
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}