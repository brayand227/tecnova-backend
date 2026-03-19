package com.tienda.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre; // "ROLE_USER" o "ROLE_ADMIN"

    private String descripcion;

    public Rol(String nombre) {
        this.nombre = nombre;
    }
}