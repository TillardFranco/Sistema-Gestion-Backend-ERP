package com.example.farmaser.controller;

import com.example.farmaser.model.dto.categoryDto.CategoryRequestDto;
import com.example.farmaser.model.dto.categoryDto.CategoryResponseDto;
import com.example.farmaser.service.ICategory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private ICategory categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<List<CategoryResponseDto>> listAll() {
        List<CategoryResponseDto> categories = categoryService.listAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto createdCategory = categoryService.save(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.update(id, categoryRequestDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


