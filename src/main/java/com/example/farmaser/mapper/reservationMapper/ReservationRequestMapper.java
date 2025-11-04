package com.example.farmaser.mapper.reservationMapper;

import com.example.farmaser.model.dto.reservationDto.ReservationRequestDto;
import com.example.farmaser.model.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationRequestMapper {
    ReservationRequestMapper INSTANCE = Mappers.getMapper(ReservationRequestMapper.class);

    ReservationEntity reservationRequestDtoToReservationEntity(ReservationRequestDto dto);
}

