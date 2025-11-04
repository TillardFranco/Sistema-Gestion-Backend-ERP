package com.example.farmaser.controller;

import com.example.farmaser.model.dto.userDto.UserResponseDto;
import com.example.farmaser.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/promote/{email}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UserResponseDto promoteToAdmin(@PathVariable String email) {
        return userService.promoteToAdmin(email);
    }

    @GetMapping("/hash/{password}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Map<String, String> generateHash(@PathVariable String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);

        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hash);
        return response;
    }
}
