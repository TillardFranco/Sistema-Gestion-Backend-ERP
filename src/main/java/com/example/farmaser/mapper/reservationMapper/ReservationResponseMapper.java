package com.example.farmaser.mapper.reservationMapper;

import com.example.farmaser.model.dto.reservationDto.ReservationResponseDto;
import com.example.farmaser.model.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationResponseMapper {
    ReservationResponseMapper INSTANCE = Mappers.getMapper(ReservationResponseMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ReservationResponseDto reservationEntityToReservationResponseDto(ReservationEntity entity);
}

