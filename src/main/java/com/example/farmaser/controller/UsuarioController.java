package com.example.farmaser.controller;


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
    public ResponseEntity<?> showAll() {
        List<UsuarioDto> getList = usuarioService.listAll();

        if (getList.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("no hay registros")
                            .object(null)
                            .build()
                    , HttpStatus.OK);

        }
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(getList)
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping("usuario")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> create(@RequestBody UsuarioDto usuarioDto){
        Usuario usuarioSave = null;
        try {
            usuarioSave = usuarioService.save(usuarioDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("usuario creado correctamente")
                    .object(usuarioMapper.usuarioToUsuarioDto(usuarioSave))
                    .build()
                    , HttpStatus.CREATED);
        } catch (DataAccessException exDt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
    @PutMapping("usuario")
    public ResponseEntity<?> update(@RequestBody UsuarioDto usuarioDto){
        Usuario usuarioUpdate = null;
        try {
            usuarioUpdate = usuarioService.save(usuarioDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Cliente actualizado correctamente")
                    .object(usuarioMapper.usuarioToUsuarioDto(usuarioUpdate))
                    .build()
                    , HttpStatus.CREATED);
        }catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try {
            UsuarioDto usuarioDto = usuarioService.findById(id);
            if (usuarioDto == null) {
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("El usuario no existe en la base de datos")
                        .object(null)
                        .build()
                        , HttpStatus.NOT_FOUND);
            }
            Usuario usuarioDelete = usuarioMapper.usuarioDtoToUsuario(usuarioDto);
            usuarioService.delete(usuarioDelete);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Usuario eliminado correctamente")
                    .object(null)
                    .build(), HttpStatus.NO_CONTENT);

        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("usuario/{id}")
    public ResponseEntity<UsuarioDto> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(usuarioService.findById(id), HttpStatus.OK);
    }
}
