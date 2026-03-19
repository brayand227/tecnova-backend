package com.tienda.backend.repository;

import com.tienda.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Métodos existentes (no los borres)
    List<Producto> findByDestacadoTrue();
    
    // 🔴 NUEVO: Agrega esta línea
    Long countByCategoriaId(Long categoriaId);
    
    // Si no existe findByCategoria, agrega también:
    List<Producto> findByCategoriaId(Long categoriaId);
}