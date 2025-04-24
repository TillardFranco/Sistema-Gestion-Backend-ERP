package com.example.farmaser.config;

import com.example.farmaser.model.entity.ERole;
import com.example.farmaser.model.entity.RoleEntity;
import com.example.farmaser.model.entity.UserEntity;
import com.example.farmaser.model.repository.RoleRepository;
import com.example.farmaser.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.admin.password}") private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        createRoles();
        createAdminUser();
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            Arrays.stream(ERole.values()).forEach(role -> {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(role);
                roleRepository.save(roleEntity);
            });
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByEmail(adminEmail)) {
            RoleEntity adminRole = roleRepository.findByName(ERole.ADMIN)
                    .orElseGet(() -> {
                        RoleEntity role = new RoleEntity();
                        role.setName(ERole.ADMIN);
                        return roleRepository.save(role);
                    });

            UserEntity admin = new UserEntity();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setName("Admin");
            admin.setLastname("System");

            admin.setRoles(Set.of(adminRole)); // Solo rol ADMIN

            userRepository.save(admin);
        }
    }
}
