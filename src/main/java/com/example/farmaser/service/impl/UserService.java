package com.example.farmaser.service.impl;


import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.userMapper.UserListMapper;
import com.example.farmaser.mapper.userMapper.UserRequestMapper;
import com.example.farmaser.mapper.userMapper.UserResponseMapper;
import com.example.farmaser.model.dto.userDto.UserRequestDto;
import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.model.entity.UserEntity;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements IUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Autowired
    private UserListMapper userListMapper;

    @Transactional
    @Override
    public List<UserResponseDto> listAll() {
            List<UserEntity> userEntities = StreamSupport.stream(
                            userRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            return userListMapper.userEntityToUserDtoList(userEntities);
    }

    @Transactional
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        return userResponseMapper.userEntityToUserResponseDto(
                userRepository.save(
                        userRequestMapper.userRequestDtoToUserEntity(userRequestDto)));
    }

    @Transactional
    @Override
    public UserResponseDto update(String email, UserRequestDto userRequestDto) {
        if (!email.equals(userRequestDto.getEmail())) {
            throw new BadRequestException("email del path no coincide con email del usuario");
        }
        return userRepository.findByEmail(email)
                .map(existingUser -> {
                    UserEntity updatedEntity = userRequestMapper.userRequestDtoToUserEntity(userRequestDto);
                    updatedEntity.setId(existingUser.getId());
                    return userResponseMapper.userEntityToUserResponseDto(userRepository.save(updatedEntity));
                })
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findByEmail(String email) {
        return userResponseMapper.userEntityToUserResponseDto(userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("No se encontro el usuario")));
    }

    @Transactional
    @Override
    public void delete(String email) {
            userRepository.delete(userRepository.findByEmail(email)
                    .orElseThrow(()-> new NotFoundException("No se encontro el usuario")));
    }

}
