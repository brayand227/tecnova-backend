package com.tienda.backend.dto;

import lombok.Data;

@Data
public class CategoriaRequest {
    private String nombre;
    private String descripcion;
    private String imagenUrl; // La URL de la imagen
    private Integer orden;
    private Boolean activa;
}