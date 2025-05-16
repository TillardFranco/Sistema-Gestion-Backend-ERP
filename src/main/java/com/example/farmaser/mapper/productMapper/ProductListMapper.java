package com.example.farmaser.mapper.productMapper;

import com.example.farmaser.model.dto.productDto.ProductResponseDto;
import com.example.farmaser.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductListMapper {
    ProductListMapper INSTANCE = Mappers.getMapper(ProductListMapper.class);

    List<ProductResponseDto> productEntityToProductList (List<ProductEntity> productEntities);
}
