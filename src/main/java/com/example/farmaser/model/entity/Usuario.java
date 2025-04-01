package com.example.farmaser.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contraseña;

    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private ERol rol = ERol.INVITADO;

    /*@OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<pedido> pedidos = new HashSet<>();
    */

}
