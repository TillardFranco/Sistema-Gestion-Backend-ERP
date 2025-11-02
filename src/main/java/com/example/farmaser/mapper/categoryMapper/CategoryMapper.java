package com.example.farmaser.mapper.categoryMapper;

import com.example.farmaser.model.dto.categoryDto.CategoryRequestDto;
import com.example.farmaser.model.dto.categoryDto.CategoryResponseDto;
import com.example.farmaser.model.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponseDto categoryEntityToCategoryResponseDto(CategoryEntity categoryEntity);

    CategoryEntity categoryRequestDtoToCategoryEntity(CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> categoryEntityListToCategoryResponseDtoList(List<CategoryEntity> categoryEntities);
}


