package com.example.farmaser.mapper.userMapper;

import com.example.farmaser.model.dto.userDto.UserRequestDto;
import com.example.farmaser.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    UserEntity userRequestDtoToUserEntity (UserRequestDto userRequestDto);
}
