package com.tienda.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String imagenUrl; // Imagen principal

    @ElementCollection
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "imagen_url", length = 500)
    private List<String> imagenesAdicionales = new ArrayList<>(); // Galería

    @ElementCollection
    @CollectionTable(name = "producto_colores", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "color", length = 7)
    private List<String> coloresDisponibles = new ArrayList<>(); // ["#FF0000", "#00FF00", "#0000FF"]

    @ElementCollection
    @CollectionTable(name = "producto_imagenes_por_color", joinColumns = @JoinColumn(name = "producto_id"))
    @MapKeyColumn(name = "color_nombre")
    @Column(name = "imagen_url")
    private Map<String, String> imagenesPorColor = new HashMap<>();

    private Boolean destacado = false;

    private Integer stock = 0;

    private String sku;

    @Column(length = 500)
    private String especificaciones; // JSON o texto con especificaciones

    @Column(name = "fecha_creacion")
    private String fechaCreacion;
}