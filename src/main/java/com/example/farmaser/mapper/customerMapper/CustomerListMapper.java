package com.example.farmaser.mapper.customerMapper;

import com.example.farmaser.model.dto.customerDto.CustomerResponseDto;
import com.example.farmaser.model.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerListMapper {
    CustomerListMapper INSTANCE = Mappers.getMapper(CustomerListMapper.class);

    List<CustomerResponseDto> customerEntityToCustomerList(List<CustomerEntity> entities);
}


