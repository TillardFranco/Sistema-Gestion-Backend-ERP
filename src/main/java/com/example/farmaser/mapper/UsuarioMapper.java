package com.example.farmaser.mapper;

import com.example.farmaser.model.dto.UsuarioDto;
import com.example.farmaser.model.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(target = "contraseña", ignore = true)
    UsuarioDto usuarioToUsuarioDto(Usuario usuario);

    Usuario usuarioDtoToUsuario(UsuarioDto usuarioDto);

    List<UsuarioDto> toUsuarioDtoList(List<Usuario> usuarios);
}
