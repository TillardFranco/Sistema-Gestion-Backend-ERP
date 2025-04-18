package com.example.farmaser.controller;


import com.example.farmaser.mapper.UserMapper;
import com.example.farmaser.model.dto.UserDto;
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
    private UserMapper userMapper;

    @GetMapping("usuarios")
    public List<UserDto> showAll() {
        return userService.listAll();
    }

    @PostMapping("usuario")
    public UserDto create(@RequestBody UserDto userDto) {
        return userMapper.userToUserDto(userService.save(userDto));
    }

    @PutMapping("usuario/{id}")
    public UserDto update(@PathVariable Integer id, @RequestBody UserDto userDto) {
        UserDto usuarioUpdate = userService.update(id, userDto);
        return userMapper.userToUserDto(userService.save(userDto));
    }

    @DeleteMapping("usuario/{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }

    @GetMapping("usuario/{id}")
    public UserDto findById(@PathVariable Integer id) {
        return userService.findById(id);
    }
}
