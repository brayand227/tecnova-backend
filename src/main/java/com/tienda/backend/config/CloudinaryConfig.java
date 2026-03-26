package com.tienda.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    
    @Bean
    public Cloudinary cloudinary() {
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");
        
        System.out.println("🔍 CLOUDINARY_URL desde entorno: " + cloudinaryUrl);
        
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            System.err.println("❌ CLOUDINARY_URL NO está configurada!");
            throw new RuntimeException("CLOUDINARY_URL no está configurada");
        }
        
        System.out.println("✅ Cloudinary URL encontrada: " + cloudinaryUrl.substring(0, Math.min(50, cloudinaryUrl.length())) + "...");
        
        try {
            Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
            System.out.println("✅ Cloudinary configurado correctamente con cloud_name: " + cloudinary.config.cloudName);
            return cloudinary;
        } catch (Exception e) {
            System.err.println("❌ Error configurando Cloudinary: " + e.getMessage());
            throw e;
        }
    }
}