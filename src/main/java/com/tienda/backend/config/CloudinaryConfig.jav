package com.tienda.backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        // Obtener la URL completa desde Railway
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");

        // Validación
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            throw new RuntimeException("❌ CLOUDINARY_URL no está configurado en Railway");
        }

        // Debug (opcional, puedes borrarlo luego)
        System.out.println("✅ Cloudinary configurado correctamente con CLOUDINARY_URL");

        // Crear instancia directamente con la URL
        return new Cloudinary(cloudinaryUrl);
    }
}