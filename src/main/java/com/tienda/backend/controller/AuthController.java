package com.tienda.backend.controller;

import com.tienda.backend.dto.JwtResponse;
import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.dto.RegistroRequest;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.security.JwtUtils;
import com.tienda.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generar token JWT real
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);
            
            // Obtener usuario para más datos
            Usuario usuario = authService.autenticarUsuario(request);
            
            JwtResponse response = new JwtResponse(
                jwt,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRoles().stream().map(rol -> rol.getNombre()).collect(Collectors.toList())
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = authService.registrarUsuario(request);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}