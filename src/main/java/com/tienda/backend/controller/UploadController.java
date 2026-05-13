package com.tienda.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("/api/upload")

public class UploadController {

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/imagen")
    public ResponseEntity<?> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            return ResponseEntity.ok(Collections.singletonMap("url", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }
    }
}