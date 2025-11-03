package com.example.farmaser.service;

import com.example.farmaser.model.dto.customerDto.CustomerRequestDto;
import com.example.farmaser.model.dto.customerDto.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomer {

    Page<CustomerResponseDto> listAll(Pageable pageable);

    List<CustomerResponseDto> listAll();

    CustomerResponseDto save(CustomerRequestDto requestDto);

    CustomerResponseDto update(Long id, CustomerRequestDto requestDto);

    CustomerResponseDto findByDni(String dni);

    Page<CustomerResponseDto> searchByName(String term, Pageable pageable);

    List<CustomerResponseDto> searchByName(String term);

    Page<CustomerResponseDto> searchByEmail(String email, Pageable pageable);

    List<CustomerResponseDto> searchByEmail(String email);

    void delete(Long id);
}


