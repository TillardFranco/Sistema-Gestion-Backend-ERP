package com.example.farmaser.service.impl;


import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.UserMapper;
import com.example.farmaser.model.dto.UserDto;
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
    private UserMapper userMapper;

    @Transactional
    @Override
    public List<UserDto> listAll() {
            List<UserEntity> userEntities = StreamSupport.stream(
                            userRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            return userMapper.toUserDtoList(userEntities);
    }

    @Transactional
    @Override
    public UserEntity save(UserDto userDto) {
        UserEntity userEntity = userMapper.userDtoToUser(userDto);
        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public UserDto update(Integer id, UserDto userDto) {
        if (!id.equals(userDto.getId())) {
            throw new BadRequestException("ID del path no coincide con ID del usuario");
        }
        if (!userRepository.existsById(userDto.getId())) {
            throw new NotFoundException("Usuario no encontrado");
        }
        UserEntity userEntity = userMapper.userDtoToUser(userDto);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.userToUserDto(savedUserEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(Integer id) {
        return userMapper.userToUserDto(userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("No se encontro el usuario")));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
            userRepository.delete(userRepository.findById(id)
                    .orElseThrow(()-> new NotFoundException("No se encontro el usuario")));
    }

}
