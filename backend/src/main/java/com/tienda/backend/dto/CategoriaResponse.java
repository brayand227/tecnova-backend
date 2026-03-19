package com.tienda.backend.dto;

import lombok.Data;

@Data
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private Integer orden;
    private Long totalProductos; // Cuántos productos tiene esta categoría
}