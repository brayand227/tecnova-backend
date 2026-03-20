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
@CrossOrigin(origins = {
    "http://localhost:5173", 
    "https://clever-semifreddo-db5b14.netlify.app",
    "https://app.netlify.com"
})
public class UploadController {
    
    @Autowired
    private Cloudinary cloudinary;
    
    @PostMapping("/imagen")
    public ResponseEntity<?> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("📤 Subiendo imagen a Cloudinary...");
            
            // Subir directamente a Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "tecnova",           // Carpeta en Cloudinary
                    "public_id", System.currentTimeMillis() // Nombre único
                )
            );
            
            // Cloudinary devuelve secure_url (HTTPS)
            String imageUrl = uploadResult.get("secure_url").toString();
            
            System.out.println("✅ Imagen subida: " + imageUrl);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            response.put("message", "Imagen subida exitosamente a Cloudinary");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al subir imagen: " + e.getMessage());
        }
    }
}