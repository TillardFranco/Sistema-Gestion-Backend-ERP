package com.example.farmaser.service;

import com.example.farmaser.model.dto.categoryDto.CategoryRequestDto;
import com.example.farmaser.model.dto.categoryDto.CategoryResponseDto;

import java.util.List;

public interface ICategory {

    List<CategoryResponseDto> listAll();

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    void delete(Long id);
}


