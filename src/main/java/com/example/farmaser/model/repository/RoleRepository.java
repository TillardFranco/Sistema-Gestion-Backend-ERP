package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.ERole;
import com.example.farmaser.model.entity.RoleEntity;
import com.example.farmaser.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(ERole name);

    boolean existsByName(ERole name);

}
