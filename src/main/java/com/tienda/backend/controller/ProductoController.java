package com.tienda.backend.controller;

import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.ProductoRepository;
import com.tienda.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"http://localhost:5173", "https://clever-semifreddo-db5b14.netlify.app"})
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @GetMapping("/destacados")
    public List<Producto> getDestacados() {
        return productoRepository.findByDestacadoTrue();
    }

    // Actualizado: ahora filtra por ID de categoría
    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> getPorCategoria(@PathVariable Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .map(categoria -> categoria.getProductos())
                .orElse(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}