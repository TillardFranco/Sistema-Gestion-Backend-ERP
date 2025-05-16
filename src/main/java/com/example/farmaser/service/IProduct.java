package com.example.farmaser.service;

import com.example.farmaser.model.dto.productDto.ProductRequestDto;
import com.example.farmaser.model.dto.productDto.ProductResponseDto;

import java.util.List;

public interface IProduct {

    List<ProductResponseDto> listAll();

    ProductResponseDto save (ProductRequestDto productRequestDto);

    ProductResponseDto update(String barcode, ProductRequestDto productRequestDto);

    ProductResponseDto findByBarcode(String barcode);

    void delete(String barcode);
}
