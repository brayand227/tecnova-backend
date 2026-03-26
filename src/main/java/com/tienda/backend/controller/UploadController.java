package com.tienda.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UploadController {

      @Autowired
    private Cloudinary cloudinary;
    
    @PostMapping("/imagen")
    public ResponseEntity<?> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "tecnova")
            );
            String imageUrl = uploadResult.get("secure_url").toString();
            System.out.println("✅ Imagen subida a Cloudinary: " + imageUrl);
            return ResponseEntity.ok(Map.of("url", imageUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}