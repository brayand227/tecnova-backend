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
        // Configuración DIRECTA con tus credenciales
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "doeyhshgo");
        config.put("api_key", "116184656834265");
        config.put("api_secret", "9S3a1c3dqc1Ll2Rc2yF5P3NFuZA");
        
        Cloudinary cloudinary = new Cloudinary(config);
        System.out.println("✅✅✅ Cloudinary BEAN creado con cloud_name: " + cloudinary.config.cloudName);
        return cloudinary;
    }
}