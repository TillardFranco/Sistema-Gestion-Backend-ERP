package com.example.farmaser.mapper.saleMapper;

import com.example.farmaser.model.dto.saleDto.SaleResponseDto;
import com.example.farmaser.model.entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {SaleItemResponseMapper.class})
public interface SaleResponseMapper {
    SaleResponseMapper INSTANCE = Mappers.getMapper(SaleResponseMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "user.id", target = "userId")
    SaleResponseDto saleEntityToSaleResponseDto(SaleEntity entity);
}

