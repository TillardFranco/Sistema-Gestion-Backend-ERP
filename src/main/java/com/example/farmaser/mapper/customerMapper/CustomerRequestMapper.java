package com.example.farmaser.mapper.customerMapper;

import com.example.farmaser.model.dto.customerDto.CustomerRequestDto;
import com.example.farmaser.model.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {
    CustomerRequestMapper INSTANCE = Mappers.getMapper(CustomerRequestMapper.class);

    CustomerEntity customerRequestDtoToCustomerEntity(CustomerRequestDto dto);
}


