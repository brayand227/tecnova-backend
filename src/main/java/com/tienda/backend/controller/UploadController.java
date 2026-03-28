package com.tienda.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = {"http://localhost:5173", "https://iridescent-bublanina-5a9677.netlify.app"})
public class UploadController {
    
    @Autowired
    private Cloudinary cloudinary;
    
    @PostMapping("/imagen")
    public ResponseEntity<?> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            // 🔍 IMPRIMIR LA CONFIGURACIÓN REAL QUE ESTÁ USANDO EL BEAN
            System.out.println("🔍 ========== DEBUG CLOUDINARY ==========");
            System.out.println("🔍 cloudinary.config.cloud_name: " + cloudinary.config.cloudName);
            System.out.println("🔍 cloudinary.config.api_key: " + cloudinary.config.apiKey);
            System.out.println("🔍 =======================================");
            
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
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}