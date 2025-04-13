package com.example.farmaser.service.impl;


import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.DataAccessException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.UsuarioMapper;
import com.example.farmaser.model.dto.UsuarioDto;
import com.example.farmaser.model.entity.Usuario;
import com.example.farmaser.model.payload.MensajeResponse;
import com.example.farmaser.model.repository.UsuarioRepository;
import com.example.farmaser.service.IUsuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuarioService implements IUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Transactional
    @Override
    public List<UsuarioDto> listAll() {
            List<Usuario> usuarios = StreamSupport.stream(
                            usuarioRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            return usuarioMapper.toUsuarioDtoList(usuarios);
    }

    @Transactional
    @Override
    public Usuario save(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.usuarioDtoToUsuario(usuarioDto);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public UsuarioDto update(Integer id, UsuarioDto usuarioDto) {
        if (!id.equals(usuarioDto.getId())) {
            throw new BadRequestException("ID del path no coincide con ID del usuario");
        }
        if (!usuarioRepository.existsById(usuarioDto.getId())) {
            throw new NotFoundException("Usuario no encontrado");
        }
        Usuario usuario = usuarioMapper.usuarioDtoToUsuario(usuarioDto);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.usuarioToUsuarioDto(savedUsuario);
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioDto findById(Integer id) {
        return usuarioMapper.usuarioToUsuarioDto(usuarioRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("No se encontro el usuario")));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
            usuarioRepository.delete(usuarioRepository.findById(id)
                    .orElseThrow(()-> new NotFoundException("No se encontro el usuario")));
    }

}
