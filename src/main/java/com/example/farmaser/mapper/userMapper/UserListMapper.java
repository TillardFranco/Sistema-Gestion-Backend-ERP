package com.example.farmaser.mapper.userMapper;

import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserListMapper {
    UserListMapper INSTANCE = Mappers.getMapper(UserListMapper.class);

    List<UserResponseDto> userEntityToUserDtoList(List<UserEntity> userEntities);
}

