package com.tienda.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nombre;
    
    private String descripcion;
    
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;  // Para la imagen principal
    
    @ElementCollection
    @CollectionTable(name = "categoria_imagenes", joinColumns = @JoinColumn(name = "categoria_id"))
    @Column(name = "imagen_url", length = 500)
    private List<String> imagenesAdicionales;  // Para galería
    
    private Boolean activa = true;
    
    @Column(name = "orden_display")
    private Integer orden = 0;
    
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Producto> productos;
}