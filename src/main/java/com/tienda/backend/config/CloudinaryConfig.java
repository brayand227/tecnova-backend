package com.tienda.backend.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    
    @Bean
    public Cloudinary cloudinary() {
        // Obtiene la URL de la variable de entorno
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");
        
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            throw new RuntimeException("CLOUDINARY_URL no está configurada en las variables de entorno");
        }
        
        return new Cloudinary(cloudinaryUrl);
    }
}