package com.example.farmaser.service;

import com.example.farmaser.model.dto.UsuarioDto;
import com.example.farmaser.model.entity.Usuario;
import com.example.farmaser.model.payload.MensajeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUsuario {

    List<UsuarioDto> listAll();

    Usuario save(UsuarioDto usuario);

    UsuarioDto update(Integer id, UsuarioDto usuarioDto);

    UsuarioDto findById(Integer id);

    void delete(Integer id);
}
