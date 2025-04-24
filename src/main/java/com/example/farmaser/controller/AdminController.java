package com.example.farmaser.controller;

import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/promote/{email}")
    public UserResponseDto promoteToAdmin(@PathVariable String email) {
        return userService.promoteToAdmin(email);
    }
}
