package com.example.farmaser.service.impl;

import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.categoryMapper.CategoryMapper;
import com.example.farmaser.model.dto.categoryDto.CategoryRequestDto;
import com.example.farmaser.model.dto.categoryDto.CategoryResponseDto;
import com.example.farmaser.model.entity.CategoryEntity;
import com.example.farmaser.model.repository.CategoryRepository;
import com.example.farmaser.service.ICategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategory {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> listAll() {
        List<CategoryEntity> categories = categoryRepository.findByActiveTrue();
        return categoryMapper.categoryEntityListToCategoryResponseDtoList(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto findById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría con ID " + id + " no encontrada"));

        return categoryMapper.categoryEntityToCategoryResponseDto(categoryEntity);
    }

    @Transactional
    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
        // Validar que el nombre no exista
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + categoryRequestDto.getName());
        }

        CategoryEntity categoryEntity = categoryMapper.categoryRequestDtoToCategoryEntity(categoryRequestDto);

        // Si active no se especifica, por defecto es true
        if (categoryEntity.getActive() == null) {
            categoryEntity.setActive(true);
        }

        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.categoryEntityToCategoryResponseDto(savedCategory);
    }

    @Transactional
    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría con ID " + id + " no encontrada"));

        // Validar que el nuevo nombre no esté en uso por otra categoría
        if (categoryRequestDto.getName() != null
                && !categoryRequestDto.getName().equals(existingCategory.getName())
                && categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new BadRequestException("Ya existe otra categoría con el nombre: " + categoryRequestDto.getName());
        }

        // Actualizar campos
        CategoryEntity updatedEntity = categoryMapper.categoryRequestDtoToCategoryEntity(categoryRequestDto);
        updatedEntity.setId(existingCategory.getId());
        updatedEntity.setCreationDate(existingCategory.getCreationDate());

        // Si active no se especifica en el DTO, mantener el valor actual
        if (updatedEntity.getActive() == null) {
            updatedEntity.setActive(existingCategory.getActive());
        }

        CategoryEntity savedCategory = categoryRepository.save(updatedEntity);
        return categoryMapper.categoryEntityToCategoryResponseDto(savedCategory);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría con ID " + id + " no encontrada"));

        // Soft delete: marcar como inactivo en lugar de eliminar físicamente
        categoryEntity.setActive(false);
        categoryRepository.save(categoryEntity);
    }
}

