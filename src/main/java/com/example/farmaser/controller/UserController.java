package com.example.farmaser.controller;


import com.example.farmaser.mapper.userMapper.UserListMapper;
import com.example.farmaser.mapper.userMapper.UserRequestMapper;
import com.example.farmaser.mapper.userMapper.UserResponseMapper;
import com.example.farmaser.model.dto.userDto.UserRequestDto;
import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Autowired
    private UserListMapper userListMapper;

    @GetMapping("usuarios")
    public List<UserResponseDto> showAll() {
        return userService.listAll();
    }

    @PostMapping("usuario")
    public UserResponseDto create(@RequestBody UserRequestDto userRequestDto) {
        return userService.save(userRequestDto);
    }

    @PutMapping("usuario/{id}")
    public UserResponseDto update(@PathVariable String email, @RequestBody UserRequestDto userRequestDto) {
        return userService.update(email, userRequestDto);
    }

    @DeleteMapping("usuario/{email}")
    public void delete(@PathVariable String email) {
        userService.delete(email);
    }

    @GetMapping("usuario/{email}")
    public UserResponseDto findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }
}
