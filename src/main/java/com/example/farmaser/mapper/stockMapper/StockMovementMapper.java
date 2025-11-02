package com.example.farmaser.mapper.stockMapper;

import com.example.farmaser.model.dto.stockDto.StockMovementRequestDto;
import com.example.farmaser.model.dto.stockDto.StockMovementResponseDto;
import com.example.farmaser.model.entity.StockMovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    StockMovementMapper INSTANCE = Mappers.getMapper(StockMovementMapper.class);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarcode", source = "product.barcode")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", expression = "java(entity.getUser().getName() + \" \" + entity.getUser().getLastname())")
    StockMovementResponseDto stockMovementEntityToStockMovementResponseDto(StockMovementEntity entity);

    List<StockMovementResponseDto> stockMovementEntityListToStockMovementResponseDtoList(List<StockMovementEntity> entities);
}


