package com.tienda.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    
    @Bean
    public Cloudinary cloudinary() {
        // Usar variables individuales para mayor claridad
        String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
        String apiKey = System.getenv("CLOUDINARY_API_KEY");
        String apiSecret = System.getenv("CLOUDINARY_API_SECRET");
        
        // Fallback a CLOUDINARY_URL si las individuales no existen
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");
        
        Map<String, String> config = new HashMap<>();
        
        if (cloudName != null && apiKey != null && apiSecret != null) {
            config.put("cloud_name", cloudName);
            config.put("api_key", apiKey);
            config.put("api_secret", apiSecret);
            System.out.println("✅ Cloudinary configurado con variables individuales");
        } else if (cloudinaryUrl != null && !cloudinaryUrl.isEmpty()) {
            System.out.println("✅ Cloudinary configurado con CLOUDINARY_URL");
            return new Cloudinary(cloudinaryUrl);
        } else {
            throw new RuntimeException("❌ No hay configuración de Cloudinary");
        }
        
        System.out.println("   cloud_name: " + config.get("cloud_name"));
        return new Cloudinary(config);
    }
}