package com.example.farmaser.mapper.saleMapper;

import com.example.farmaser.model.dto.saleDto.SaleItemResponseDto;
import com.example.farmaser.model.entity.SaleItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SaleItemResponseMapper {
    SaleItemResponseMapper INSTANCE = Mappers.getMapper(SaleItemResponseMapper.class);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    SaleItemResponseDto saleItemEntityToSaleItemResponseDto(SaleItemEntity entity);
}

