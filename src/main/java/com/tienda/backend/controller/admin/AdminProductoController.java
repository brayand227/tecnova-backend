package com.tienda.backend.controller.admin;

import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.ProductoRepository;
import com.tienda.backend.repository.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/productos")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Listar todos los productos (para admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        
        // Asegurar que las imágenes por color se devuelvan como mapa
        productos.forEach(producto -> {
            if (producto.getImagenesPorColor() == null) {
                producto.setImagenesPorColor(new HashMap<>());
            }
        });
        
        return productos;
    }
    
    // Obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoRepository.findById(id)
            .map(producto -> {
                if (producto.getImagenesPorColor() == null) {
                    producto.setImagenesPorColor(new HashMap<>());
                }
                return ResponseEntity.ok(producto);
            })
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
            
            // Validar y procesar imágenes por color
            if (producto.getImagenesPorColor() == null) {
                producto.setImagenesPorColor(new HashMap<>());
            }
            
            // Limpiar valores nulos en el mapa de imágenes por color
            producto.getImagenesPorColor().entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().trim().isEmpty());
            
            // Validar que los colores tengan imágenes válidas
            for (Map.Entry<String, String> entry : producto.getImagenesPorColor().entrySet()) {
                String color = entry.getKey();
                String imagenUrl = entry.getValue();
                if (imagenUrl == null || imagenUrl.trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                        .body("El color " + color + " debe tener una imagen válida");
                }
            }
            
            Producto nuevoProducto = productoRepository.save(producto);
            
            // Asegurar que la respuesta incluya el mapa de imágenes
            if (nuevoProducto.getImagenesPorColor() == null) {
                nuevoProducto.setImagenesPorColor(new HashMap<>());
            }
            
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }
    
    // Actualizar producto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id)
            .map(producto -> {
                // Actualizar campos básicos
                producto.setNombre(productoActualizado.getNombre());
                producto.setDescripcion(productoActualizado.getDescripcion());
                producto.setPrecio(productoActualizado.getPrecio());
                producto.setCategoria(productoActualizado.getCategoria());
                producto.setImagenUrl(productoActualizado.getImagenUrl());
                producto.setImagenesAdicionales(productoActualizado.getImagenesAdicionales());
                producto.setDestacado(productoActualizado.getDestacado());
                producto.setStock(productoActualizado.getStock());
                producto.setSku(productoActualizado.getSku());
                producto.setEspecificaciones(productoActualizado.getEspecificaciones());
                producto.setColoresDisponibles(productoActualizado.getColoresDisponibles());
                
                // Actualizar el mapa de imágenes por color
                if (productoActualizado.getImagenesPorColor() != null) {
                    // Limpiar valores nulos
                    productoActualizado.getImagenesPorColor().entrySet().removeIf(entry -> 
                        entry.getValue() == null || entry.getValue().trim().isEmpty()
                    );
                    producto.setImagenesPorColor(productoActualizado.getImagenesPorColor());
                } else {
                    producto.setImagenesPorColor(new HashMap<>());
                }
                
                // Validar que todos los colores disponibles tengan una imagen asociada (opcional)
                if (producto.getColoresDisponibles() != null && !producto.getColoresDisponibles().isEmpty()) {
                    for (String color : producto.getColoresDisponibles()) {
                        if (!producto.getImagenesPorColor().containsKey(color)) {
                            // No es obligatorio, solo se puede loguear o ignorar
                            System.out.println("Advertencia: El color " + color + " no tiene imagen asociada");
                        }
                    }
                }
                
                Producto productoGuardado = productoRepository.save(producto);
                
                // Asegurar que la respuesta incluya el mapa de imágenes
                if (productoGuardado.getImagenesPorColor() == null) {
                    productoGuardado.setImagenesPorColor(new HashMap<>());
                }
                
                return ResponseEntity.ok(productoGuardado);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Actualizar parcialmente un producto (PATCH) - Útil para actualizar solo algunos campos
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProductoParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return productoRepository.findById(id)
            .map(producto -> {
                try {
                    // Actualizar campos específicos según lo que llegue en el request
                    if (updates.containsKey("nombre")) {
                        producto.setNombre((String) updates.get("nombre"));
                    }
                    if (updates.containsKey("descripcion")) {
                        producto.setDescripcion((String) updates.get("descripcion"));
                    }
                    if (updates.containsKey("precio")) {
                        Object precioObj = updates.get("precio");
                        if (precioObj instanceof Number) {
                            producto.setPrecio(((Number) precioObj).doubleValue());
                        }
                    }
                    if (updates.containsKey("imagenUrl")) {
                        producto.setImagenUrl((String) updates.get("imagenUrl"));
                    }
                    if (updates.containsKey("imagenesAdicionales")) {
                        producto.setImagenesAdicionales((List<String>) updates.get("imagenesAdicionales"));
                    }
                    if (updates.containsKey("destacado")) {
                        producto.setDestacado((Boolean) updates.get("destacado"));
                    }
                    if (updates.containsKey("stock")) {
                        Object stockObj = updates.get("stock");
                        if (stockObj instanceof Number) {
                            producto.setStock(((Number) stockObj).intValue());
                        }
                    }
                    if (updates.containsKey("sku")) {
                        producto.setSku((String) updates.get("sku"));
                    }
                    if (updates.containsKey("especificaciones")) {
                        producto.setEspecificaciones((String) updates.get("especificaciones"));
                    }
                    if (updates.containsKey("coloresDisponibles")) {
                        producto.setColoresDisponibles((List<String>) updates.get("coloresDisponibles"));
                    }
                    if (updates.containsKey("imagenesPorColor")) {
                        Map<String, String> imagenesPorColor = (Map<String, String>) updates.get("imagenesPorColor");
                        // Limpiar valores nulos
                        if (imagenesPorColor != null) {
                            imagenesPorColor.entrySet().removeIf(entry -> 
                                entry.getValue() == null || entry.getValue().trim().isEmpty()
                            );
                            producto.setImagenesPorColor(imagenesPorColor);
                        } else {
                            producto.setImagenesPorColor(new HashMap<>());
                        }
                    }
                    
                    Producto productoGuardado = productoRepository.save(producto);
                    return ResponseEntity.ok(productoGuardado);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Error al actualizar producto: " + e.getMessage());
                }
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
                return ResponseEntity.ok(Map.of(
                    "message", "Producto eliminado correctamente",
                    "id", id
                ));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Endpoint adicional para actualizar solo las imágenes por color
    @PatchMapping("/{id}/imagenes-por-color")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarImagenesPorColor(
            @PathVariable Long id, 
            @RequestBody Map<String, String> imagenesPorColor) {
        return productoRepository.findById(id)
            .map(producto -> {
                // Limpiar valores nulos
                if (imagenesPorColor != null) {
                    imagenesPorColor.entrySet().removeIf(entry -> 
                        entry.getValue() == null || entry.getValue().trim().isEmpty()
                    );
                    producto.setImagenesPorColor(imagenesPorColor);
                } else {
                    producto.setImagenesPorColor(new HashMap<>());
                }
                
                Producto productoGuardado = productoRepository.save(producto);
                return ResponseEntity.ok(Map.of(
                    "message", "Imágenes por color actualizadas correctamente",
                    "imagenesPorColor", productoGuardado.getImagenesPorColor()
                ));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}