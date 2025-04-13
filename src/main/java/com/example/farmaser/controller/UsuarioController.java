package com.example.farmaser.controller;


import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.mapper.UsuarioMapper;
import com.example.farmaser.model.dto.UsuarioDto;
import com.example.farmaser.model.entity.Usuario;
import com.example.farmaser.model.payload.MensajeResponse;
import com.example.farmaser.service.IUsuario;
import com.example.farmaser.service.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @GetMapping("usuarios")
    public List<UsuarioDto> showAll() {
        return usuarioService.listAll();
    }

    @PostMapping("usuario")
    public UsuarioDto create(@RequestBody UsuarioDto usuarioDto) {
        return usuarioMapper.usuarioToUsuarioDto(usuarioService.save(usuarioDto));
    }

    @PutMapping("usuario/{id}")
    public UsuarioDto update(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuarioUpdate = usuarioService.update(id, usuarioDto);
        return usuarioMapper.usuarioToUsuarioDto(usuarioService.save(usuarioDto));
    }

    @DeleteMapping("usuario/{id}")
    public void delete(@PathVariable Integer id) {
        usuarioService.delete(id);
    }

    @GetMapping("usuario/{id}")
    public UsuarioDto findById(@PathVariable Integer id) {
        return usuarioService.findById(id);
    }
}
