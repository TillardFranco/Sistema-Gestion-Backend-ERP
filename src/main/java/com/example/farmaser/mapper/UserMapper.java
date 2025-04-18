package com.example.farmaser.mapper;

import com.example.farmaser.model.dto.UserDto;
import com.example.farmaser.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDto userToUserDto(UserEntity userEntity);

    UserEntity userDtoToUser(UserDto userDto);

    List<UserDto> toUserDtoList(List<UserEntity> userEntities);
}
