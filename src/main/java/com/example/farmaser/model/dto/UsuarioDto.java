package com.example.farmaser.model.dto;

import com.example.farmaser.model.entity.ERol;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
@Builder
public class UsuarioDto implements Serializable {

    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private Timestamp fechaCreacion;
    private ERol rol;
}
