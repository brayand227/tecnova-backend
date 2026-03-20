package com.tienda.backend.controller.admin;

import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.ProductoRepository;
import com.tienda.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/productos")
@CrossOrigin(origins = {"http://localhost:5173", "https://clever-semifreddo-db5b14.netlify.app"})
public class AdminProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Listar todos los productos (para admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    
    // Obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Crear nuevo producto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            // Verificar que la categoría existe si se asignó
            if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
                categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            }
            
            Producto nuevoProducto = productoRepository.save(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Actualizar producto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setNombre(productoActualizado.getNombre());
                producto.setDescripcion(productoActualizado.getDescripcion());
                producto.setPrecio(productoActualizado.getPrecio());
                producto.setCategoria(productoActualizado.getCategoria());
                producto.setImagenUrl(productoActualizado.getImagenUrl());
                producto.setDestacado(productoActualizado.getDestacado());
                producto.setStock(productoActualizado.getStock());
                producto.setSku(productoActualizado.getSku());
                
                productoRepository.save(producto);
                return ResponseEntity.ok(producto);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Eliminar producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
            .map(producto -> {
                productoRepository.delete(producto);
                return ResponseEntity.ok("Producto eliminado");
            })
            .orElse(ResponseEntity.notFound().build());
    }
}