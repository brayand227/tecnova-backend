package main.java.com.tienda.backend.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    
    @Bean
    public Cloudinary cloudinary() {
        // Toma la URL de la variable de entorno CLOUDINARY_URL
        return new Cloudinary(System.getenv("CLOUDINARY_URL"));
    }
}