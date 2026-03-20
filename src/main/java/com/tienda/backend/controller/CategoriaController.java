package com.tienda.backend.controller;

import com.tienda.backend.model.Categoria;
import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = {"http://localhost:5173", "https://clever-semifreddo-db5b14.netlify.app"})
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // TODOS pueden ver las categorías activas
    @GetMapping
    public List<Categoria> getCategoriasActivas() {
        return categoriaRepository.findByActivaTrueOrderByOrdenAsc();
    }

    // Ver una categoría específica
    @GetMapping("/{id}")
    public Categoria getCategoria(@PathVariable Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // Ver productos de una categoría
    @GetMapping("/{id}/productos")
    public List<Producto> getProductosPorCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        return categoria != null ? categoria.getProductos() : List.of();
    }
}