package com.example.farmaser.service;

import com.example.farmaser.model.dto.productDto.ProductRequestDto;
import com.example.farmaser.model.dto.productDto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProduct {

    Page<ProductResponseDto> listAll(Pageable pageable);

    List<ProductResponseDto> listAll();

    ProductResponseDto save(ProductRequestDto productRequestDto);

    ProductResponseDto update(String barcode, ProductRequestDto productRequestDto);

    ProductResponseDto findByBarcode(String barcode);

    Page<ProductResponseDto> searchByName(String name, Pageable pageable);

    List<ProductResponseDto> searchByName(String name);

    void delete(String barcode);

    ProductResponseDto updateStock(String barcode, Integer stock);

    List<ProductResponseDto> getLowStockProducts();
}
