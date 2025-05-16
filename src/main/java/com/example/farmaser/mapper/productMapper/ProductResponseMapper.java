package com.example.farmaser.mapper.productMapper;

import com.example.farmaser.model.dto.productDto.ProductResponseDto;
import com.example.farmaser.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {
    ProductResponseMapper INSTANCE = Mappers.getMapper(ProductResponseMapper.class);

    ProductResponseDto productEntityToProductDto (ProductEntity productEntity);
}
