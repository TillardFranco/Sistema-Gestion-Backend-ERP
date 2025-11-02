package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String name);
    boolean existsByName(String name);

    List<CategoryEntity> findByActiveTrue();
}


