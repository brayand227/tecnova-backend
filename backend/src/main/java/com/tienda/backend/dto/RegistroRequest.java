package com.tienda.backend.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    private String username;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
}