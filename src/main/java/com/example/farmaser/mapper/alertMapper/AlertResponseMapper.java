package com.example.farmaser.mapper.alertMapper;

import com.example.farmaser.model.dto.alertDto.AlertResponseDto;
import com.example.farmaser.model.entity.AlertEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AlertResponseMapper {
    AlertResponseMapper INSTANCE = Mappers.getMapper(AlertResponseMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    AlertResponseDto alertEntityToAlertResponseDto(AlertEntity entity);
}

