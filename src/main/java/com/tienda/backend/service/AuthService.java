package com.tienda.backend.service;

import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.dto.RegistroRequest;
import com.tienda.backend.model.Rol;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.repository.RolRepository;
import com.tienda.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(RegistroRequest request) {
        // Verificar si ya existe
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Error: El username ya existe");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: El email ya existe");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());

        // Asignar rol USER por defecto
        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));

        List<Rol> roles = new ArrayList<>();
        roles.add(rolUser);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    // En AuthService.java, modifica o agrega este método:
    public Usuario autenticarUsuario(LoginRequest request) {
        return usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}