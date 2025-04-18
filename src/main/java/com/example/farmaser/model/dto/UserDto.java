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
public class UserDto implements Serializable {

    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Timestamp fechaCreacion;
    private ERol rol;
}
