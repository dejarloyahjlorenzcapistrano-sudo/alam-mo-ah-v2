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

            private CorsRegistry registry;

            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                this.registry = registry;

                registry.addMapping("/**")
                        .allowedOrigins(
                                "https://alam-mo-ah-v2.vercel.app",
                                "https://alam-mo-ah-v2-ri1xhsgqq.vercel.app",
                                "https://alam-mo-ah-v2-kyu4fpabg.vercel.app"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}