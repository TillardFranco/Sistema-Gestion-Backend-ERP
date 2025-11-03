package com.example.farmaser.mapper.customerMapper;

import com.example.farmaser.model.dto.customerDto.CustomerResponseDto;
import com.example.farmaser.model.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {
    CustomerResponseMapper INSTANCE = Mappers.getMapper(CustomerResponseMapper.class);

    CustomerResponseDto customerEntityToCustomerResponseDto(CustomerEntity entity);
}


