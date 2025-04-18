package com.example.farmaser.service;

import com.example.farmaser.model.dto.UserDto;
import com.example.farmaser.model.entity.UserEntity;

import java.util.List;

public interface IUser {

    List<UserDto> listAll();

    UserEntity save(UserDto user);

    UserDto update(Integer id, UserDto userDto);

    UserDto findById(Integer id);

    void delete(Integer id);
}
