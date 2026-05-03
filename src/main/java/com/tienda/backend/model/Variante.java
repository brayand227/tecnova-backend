package com.tienda.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "variantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String color;           // Ej: "Negro", "Blanco", "Azul"
    
    private String colorCodigo;     // Ej: "#000000", "#FFFFFF", "#0066cc"
    
    private String almacenamiento;  // Ej: "128GB", "256GB", "512GB"
    
    private Double precio;          // Precio específico de esta variante
    
    private Integer stock;          // Stock específico de esta variante
    
    private String imagenUrl;       // Imagen específica de esta variante
}