
package com.tienda.backend.controller;

import com.tienda.backend.dto.CategoriaRequest;
import com.tienda.backend.model.Categoria;
import com.tienda.backend.repository.CategoriaRepository;
import com.tienda.backend.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categorias")
@CrossOrigin(origins = {"http://localhost:5173", "https://clever-semifreddo-db5b14.netlify.app"})
public class AdminCategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    // 1. CREAR nueva categoría (solo admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaRequest request) {

        // Verificar si ya existe una categoría con ese nombre
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            return ResponseEntity.badRequest()
                    .body("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        // Crear nueva categoría
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setImagenUrl(request.getImagenUrl());
        categoria.setOrden(request.getOrden() != null ? request.getOrden() : 0);
        categoria.setActiva(request.getActiva() != null ? request.getActiva() : true);

        Categoria nuevaCategoria = categoriaRepository.save(categoria);

        return ResponseEntity.ok(nuevaCategoria);
    }

    // 2. ACTUALIZAR categoría existente (solo admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {

        return categoriaRepository.findById(id)
                .map(categoria -> {
                    // Actualizar campos
                    categoria.setNombre(request.getNombre());
                    categoria.setDescripcion(request.getDescripcion());
                    categoria.setImagenUrl(request.getImagenUrl());
                    categoria.setOrden(request.getOrden());
                    categoria.setActiva(request.getActiva());

                    Categoria categoriaActualizada = categoriaRepository.save(categoria);
                    return ResponseEntity.ok(categoriaActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. ACTIVAR/DESACTIVAR categoría (solo admin)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activa) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setActiva(activa);
                    categoriaRepository.save(categoria);
                    return ResponseEntity.ok("Estado actualizado a: " + (activa ? "activa" : "inactiva"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {

        // Versión corregida sin Optional
        Categoria categoria = categoriaRepository.findById(id).orElse(null);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Contar productos usando el método del repository
            Long cantidadProductos = productoRepository.countByCategoriaId(id);

            System.out.println("🔍 Eliminando categoría: " + categoria.getNombre() + " (ID: " + id + ")");
            System.out.println("📦 Productos encontrados: " + cantidadProductos);

            if (cantidadProductos > 0) {
                String mensaje = String.format(
                        "No se puede eliminar: la categoría '%s' tiene %d %s asociados. " +
                                "Primero elimina o recategoriza los productos.",
                        categoria.getNombre(),
                        cantidadProductos,
                        cantidadProductos == 1 ? "producto" : "productos");
                return ResponseEntity.badRequest().body(mensaje);
            }

            // Si no hay productos, eliminar
            categoriaRepository.delete(categoria);
            return ResponseEntity.ok("Categoría eliminada exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Categoria> getTodasCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();

        // Forzar la carga de productos para asegurar conteo correcto
        categorias.forEach(categoria -> {
            if (categoria.getProductos() != null) {
                categoria.getProductos().size(); // Forzar inicialización
            }
        });

        return categorias;
    }
}