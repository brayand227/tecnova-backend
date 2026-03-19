package com.tienda.backend.repository;

import com.tienda.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categoría por nombre (útil para validar)
    Optional<Categoria> findByNombre(String nombre);

    // Verificar si existe una categoría con ese nombre
    Boolean existsByNombre(String nombre);

    // Obtener solo categorías activas, ordenadas
    List<Categoria> findByActivaTrueOrderByOrdenAsc();

    // Buscar categorías que contengan texto en su nombre
    List<Categoria> findByNombreContainingIgnoreCase(String texto);
}