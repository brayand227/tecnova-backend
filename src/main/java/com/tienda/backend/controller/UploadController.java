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
@CrossOrigin(origins = "*")
public class UploadController {
    
    @Autowired
    private Cloudinary cloudinary;
    
    @PostMapping("/imagen")
    public ResponseEntity<?> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("🔍 Usando Cloudinary bean con cloud_name: " + cloudinary.config.cloudName);
            
            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "tecnova")
            );
            
            String imageUrl = (String) uploadResult.get("secure_url");
            System.out.println("✅ Imagen subida: " + imageUrl);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}