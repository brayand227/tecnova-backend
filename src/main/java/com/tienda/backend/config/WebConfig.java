package com.tienda.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",
                    "https://iridescent-bublanina-5a9677.netlify.app",
                    "https://5c91b5cb.tecnova-fronted.pages.dev",
                    "https://f8179932.tecnova-fronted.pages.dev",
                    "https://tecnova-backend.onrender.com"      // 👈 TU BACKEND
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 👈 Lista explícita, NO "*"
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}