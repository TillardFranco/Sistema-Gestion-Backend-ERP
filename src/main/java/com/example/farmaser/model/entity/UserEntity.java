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
@Table(name = "user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private ERol rol = ERol.INVITED;

    /*@OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<pedido> pedidos = new HashSet<>();
    */

}
