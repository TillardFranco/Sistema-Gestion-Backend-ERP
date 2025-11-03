package com.example.farmaser.mapper.saleMapper;

import com.example.farmaser.model.dto.saleDto.SaleItemRequestDto;
import com.example.farmaser.model.entity.SaleItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SaleItemRequestMapper {
    SaleItemRequestMapper INSTANCE = Mappers.getMapper(SaleItemRequestMapper.class);

    SaleItemEntity saleItemRequestDtoToSaleItemEntity(SaleItemRequestDto dto);
}

