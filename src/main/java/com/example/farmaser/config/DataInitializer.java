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
        migrateExistingUsers();
        createAdminUser();
    }

    private void createRoles() {
        // Crear todos los roles nuevos si no existen
        Arrays.stream(ERole.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(role);
                roleRepository.save(roleEntity);
            }
        });
    }

    private void migrateExistingUsers() {
        // Migrar usuarios ADMIN existentes a SUPER_ADMIN
        RoleEntity superAdminRole = roleRepository.findByName(ERole.SUPER_ADMIN)
                .orElseGet(() -> {
                    RoleEntity role = new RoleEntity();
                    role.setName(ERole.SUPER_ADMIN);
                    return roleRepository.save(role);
                });

        RoleEntity adminRole = roleRepository.findByName(ERole.ADMIN).orElse(null);
        if (adminRole != null) {
            userRepository.findAll().forEach(user -> {
                if (user.getRoles().contains(adminRole)) {
                    user.getRoles().remove(adminRole);
                    user.getRoles().add(superAdminRole);
                    userRepository.save(user);
                }
            });
        }

        // Migrar usuarios USER existentes a CASHIER
        RoleEntity cashierRole = roleRepository.findByName(ERole.CASHIER)
                .orElseGet(() -> {
                    RoleEntity role = new RoleEntity();
                    role.setName(ERole.CASHIER);
                    return roleRepository.save(role);
                });

        RoleEntity userRole = roleRepository.findByName(ERole.USER).orElse(null);
        if (userRole != null) {
            userRepository.findAll().forEach(user -> {
                if (user.getRoles().contains(userRole) && !user.getRoles().contains(superAdminRole)) {
                    user.getRoles().remove(userRole);
                    user.getRoles().add(cashierRole);
                    userRepository.save(user);
                }
            });
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByEmail(adminEmail)) {
            RoleEntity superAdminRole = roleRepository.findByName(ERole.SUPER_ADMIN)
                    .orElseGet(() -> {
                        RoleEntity role = new RoleEntity();
                        role.setName(ERole.SUPER_ADMIN);
                        return roleRepository.save(role);
                    });

            UserEntity admin = new UserEntity();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setName("Admin");
            admin.setLastname("System");

            admin.setRoles(Set.of(superAdminRole)); // Rol SUPER_ADMIN

            userRepository.save(admin);
        }
    }
}
