package com.tienda.backend.controller.admin;

import com.tienda.backend.model.Producto;
import com.tienda.backend.model.Variante;
import com.tienda.backend.repository.ProductoRepository;
import com.tienda.backend.repository.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/productos")
@CrossOrigin(origins = { "http://localhost:5173", "https://tecnova-fronted.pages.dev",
        "https://tecnova-backend.onrender.com" }, allowCredentials = "true")
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
        return productos;
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

            // Asegurar que el precio sea un número sin decimales
            if (producto.getPrecio() != null) {
                producto.setPrecio(Math.floor(producto.getPrecio()));
            }

            // Limpiar variantes nulas o vacías
            if (producto.getVariantes() != null) {
                producto.getVariantes()
                        .removeIf(variante -> variante.getColor() == null || variante.getColor().trim().isEmpty() ||
                                variante.getAlmacenamiento() == null || variante.getAlmacenamiento().trim().isEmpty());
            }

            Producto nuevoProducto = productoRepository.save(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }

    // Actualizar producto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    // Actualizar campos básicos
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());

                    // Precio (redondear a entero)
                    if (productoActualizado.getPrecio() != null) {
                        producto.setPrecio(Math.floor(productoActualizado.getPrecio()));
                    } else {
                        producto.setPrecio(productoActualizado.getPrecio());
                    }

                    producto.setCategoria(productoActualizado.getCategoria());
                    producto.setImagenUrl(productoActualizado.getImagenUrl());
                    producto.setImagenesAdicionales(productoActualizado.getImagenesAdicionales());
                    producto.setDestacado(productoActualizado.getDestacado());
                    producto.setStock(productoActualizado.getStock());
                    producto.setSku(productoActualizado.getSku());
                    producto.setEspecificaciones(productoActualizado.getEspecificaciones());

                    // Actualizar variantes
                    if (productoActualizado.getVariantes() != null) {
                        productoActualizado.getVariantes().removeIf(variante -> variante.getColor() == null
                                || variante.getColor().trim().isEmpty() ||
                                variante.getAlmacenamiento() == null || variante.getAlmacenamiento().trim().isEmpty());
                        producto.setVariantes(productoActualizado.getVariantes());
                    } else {
                        producto.setVariantes(null);
                    }

                    Producto productoGuardado = productoRepository.save(producto);
                    return ResponseEntity.ok(productoGuardado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar parcialmente un producto (PATCH)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProductoParcial(@PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return productoRepository.findById(id)
                .map(producto -> {
                    try {
                        if (updates.containsKey("nombre")) {
                            producto.setNombre((String) updates.get("nombre"));
                        }
                        if (updates.containsKey("descripcion")) {
                            producto.setDescripcion((String) updates.get("descripcion"));
                        }
                        if (updates.containsKey("precio")) {
                            Object precioObj = updates.get("precio");
                            if (precioObj instanceof Number) {
                                double precio = ((Number) precioObj).doubleValue();
                                producto.setPrecio(Math.floor(precio));
                            } else if (precioObj instanceof String) {
                                try {
                                    double precio = Double.parseDouble((String) precioObj);
                                    producto.setPrecio(Math.floor(precio));
                                } catch (NumberFormatException e) {
                                    return ResponseEntity.badRequest().body("Formato de precio inválido");
                                }
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

                        // 🔥 MANEJO CORREGIDO DE VARIANTES
                        if (updates.containsKey("variantes")) {
                            List<Variante> nuevasVariantes = objectMapper.convertValue(
                                    updates.get("variantes"),
                                    objectMapper.getTypeFactory().constructCollectionType(List.class, Variante.class));

                            if (nuevasVariantes != null) {
                                // Limpiar variantes vacías
                                nuevasVariantes.removeIf(v -> v.getColor() == null || v.getColor().trim().isEmpty() ||
                                        v.getAlmacenamiento() == null || v.getAlmacenamiento().trim().isEmpty());

                                // Limpiar la lista existente y agregar las nuevas
                                producto.getVariantes().clear();
                                for (Variante v : nuevasVariantes) {
                                    // No enviar ID para variantes nuevas
                                    v.setId(null);
                                    producto.getVariantes().add(v);
                                }
                            } else {
                                producto.getVariantes().clear();
                            }
                        }

                        Producto productoGuardado = productoRepository.save(producto);
                        return ResponseEntity.ok(productoGuardado);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                            "id", id));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}