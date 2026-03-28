package com.tienda.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = {"http://localhost:5173", "https://iridescent-bublanina-5a9677.netlify.app"})
public class UploadController {
    
    @PostMapping("/imagen")
    public ResponseEntity<?> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            // Configuración DIRECTA de Cloudinary (para evitar problemas de inyección)
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "doeyhshgo");
            config.put("api_key", "116184656834265");
            config.put("api_secret", "9S3a1c3dqc1Ll2Rc2yF5P3NFuZA");
            
            Cloudinary cloudinary = new Cloudinary(config);
            
            System.out.println("✅ Cloudinary configurado DIRECTAMENTE con cloud_name: doeyhshgo");
            
            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "tecnova",
                    "public_id", System.currentTimeMillis()
                )
            );
            
            String imageUrl = uploadResult.get("secure_url").toString();
            System.out.println("✅ Imagen subida: " + imageUrl);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error subiendo imagen: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}