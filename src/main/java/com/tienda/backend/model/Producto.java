package com.tienda.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(length = 2000)
    private String descripcion;
    
    @Column(nullable = false)
    private Double precio;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;
    
    @ElementCollection
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "imagen_url", length = 500)
    private List<String> imagenesAdicionales = new ArrayList<>();
    
    // Colores disponibles (legacy, mantener por compatibilidad)
    @ElementCollection
    @CollectionTable(name = "producto_colores", joinColumns = @JoinColumn(name = "producto_id"))
    private List<String> coloresDisponibles = new ArrayList<>();
    
    // NUEVO: Variantes con color (nombre), almacenamiento, precio específico, etc.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "producto_id")
    private List<Variante> variantes = new ArrayList<>();
    
    private Boolean destacado = false;
    
    private Integer stock = 0;
    
    private String sku;
    
    @Column(length = 500)
    private String especificaciones;
    
    @Column(name = "fecha_creacion")
    private String fechaCreacion;
}