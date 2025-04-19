package com.example.farmaser.mapper.userMapper;

import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    UserResponseDto userEntityToUserResponseDto(UserEntity userEntity);
}
