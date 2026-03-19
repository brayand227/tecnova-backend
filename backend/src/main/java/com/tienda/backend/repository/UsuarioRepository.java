package com.tienda.backend.repository;

import com.tienda.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por username (para login)
    Optional<Usuario> findByUsername(String username);
    
    // Buscar por email (para registro)
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}