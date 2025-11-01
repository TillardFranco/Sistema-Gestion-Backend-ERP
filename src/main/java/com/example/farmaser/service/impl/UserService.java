package com.example.farmaser.service.impl;


import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.userMapper.UserListMapper;
import com.example.farmaser.mapper.userMapper.UserRequestMapper;
import com.example.farmaser.mapper.userMapper.UserResponseMapper;
import com.example.farmaser.model.dto.userDto.UserRequestDto;
import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.model.entity.ERole;
import com.example.farmaser.model.entity.RoleEntity;
import com.example.farmaser.model.entity.UserEntity;
import com.example.farmaser.model.repository.RoleRepository;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements IUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Autowired
    private UserListMapper userListMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        UserEntity userEntity = userRequestMapper.userRequestDtoToUserEntity(userRequestDto);
        userEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        RoleEntity userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        userEntity.setRoles(Set.of(userRole));

        return userResponseMapper.userEntityToUserResponseDto(
                userRepository.save(userEntity));
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
                    if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
                        updatedEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
                    } else {
                        updatedEntity.setPassword(existingUser.getPassword());
                    }
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

    @Transactional
    public UserResponseDto promoteToAdmin(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        RoleEntity adminRole = roleRepository.findByName(ERole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

        user.getRoles().add(adminRole);
        return userResponseMapper.userEntityToUserResponseDto(userRepository.save(user));
    }

}
