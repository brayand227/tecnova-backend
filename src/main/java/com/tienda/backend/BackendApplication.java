package com.tienda.backend;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    
    @Bean
    public Cloudinary cloudinary() {
        System.out.println("🚀🚀🚀 CREANDO CLOUDINARY BEAN DESDE LA CLASE PRINCIPAL");
        
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "doeyhshgo");
        config.put("api_key", "116184656834265");
        config.put("api_secret", "9S3a1c3dqc1Ll2Rc2yF5P3NFuZA");
        
        Cloudinary cloudinary = new Cloudinary(config);
        System.out.println("✅✅✅ Cloudinary creado con cloud_name: " + cloudinary.config.cloudName);
        return cloudinary;
    }
}