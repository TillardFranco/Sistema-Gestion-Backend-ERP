package com.example.farmaser.service;

import com.example.farmaser.model.dto.UsuarioDto;
import com.example.farmaser.model.entity.Usuario;

import java.util.List;

public interface IUsuario {

    List<UsuarioDto> listAll();

    Usuario save(UsuarioDto usuario);

    UsuarioDto findById(Integer id);

    void delete(Usuario usuario);
}
