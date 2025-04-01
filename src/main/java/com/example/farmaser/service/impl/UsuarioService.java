package com.example.farmaser.service.impl;


import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.UsuarioMapper;
import com.example.farmaser.model.dto.UsuarioDto;
import com.example.farmaser.model.entity.Usuario;
import com.example.farmaser.model.repository.UsuarioRepository;
import com.example.farmaser.service.IUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;
    public List<UsuarioDto> listAll() {
        /*return usuarioRepository.findAll().stream()
                .map(usuarioMapper::usuarioToUsuarioDto)
                .collect(Collectors.ToList());*/
        return null;
    }

    @Transactional
    @Override
    public Usuario save(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.usuarioDtoToUsuario(usuarioDto);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioDto findById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se ha encontrado un usuario con dicha id"));
        return usuarioMapper.usuarioToUsuarioDto(usuario);
    }

    @Transactional
    @Override
    public void delete(Usuario usuario){
        usuarioRepository.delete(usuario);
    }

}
