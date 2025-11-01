package com.example.farmaser.service;

import com.example.farmaser.model.dto.userDto.UserRequestDto;
import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.model.entity.UserEntity;

import java.util.List;

public interface IUser {

    List<UserResponseDto> listAll();

    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto update(String email, UserRequestDto userRequestDto);

    UserResponseDto findByEmail(String email);

    void delete(String email);
}
