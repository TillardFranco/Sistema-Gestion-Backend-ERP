package com.example.farmaser.mapper.productMapper;

import com.example.farmaser.model.dto.productDto.ProductRequestDto;
import com.example.farmaser.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductRequestMapper {
    ProductRequestMapper INSTANCE = Mappers.getMapper(ProductRequestMapper.class);

    ProductEntity productRequestDtoToProductEntity (ProductRequestDto productRequestDto);

}
