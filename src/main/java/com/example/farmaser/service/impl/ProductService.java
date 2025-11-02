package com.example.farmaser.service.impl;

import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.productMapper.ProductListMapper;
import com.example.farmaser.mapper.productMapper.ProductRequestMapper;
import com.example.farmaser.mapper.productMapper.ProductResponseMapper;
import com.example.farmaser.model.dto.productDto.ProductRequestDto;
import com.example.farmaser.model.dto.productDto.ProductResponseDto;
import com.example.farmaser.model.entity.ProductEntity;
import com.example.farmaser.model.repository.ProductRepository;
import com.example.farmaser.service.IProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProduct {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private ProductRequestMapper productRequestMapper;

    @Autowired
    private ProductListMapper productListMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> listAll(Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findByActiveTrue(pageable);
        List<ProductResponseDto> dtos = productPage.getContent().stream()
                .map(productResponseMapper::productEntityToProductDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, productPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDto> listAll() {
        List<ProductEntity> productEntities = productRepository.findByActiveTrue();
        return productListMapper.productEntityToProductList(productEntities);
    }

    @Transactional
    @Override
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        // Validar que el código de barras no exista (si se proporciona)
        if (productRequestDto.getBarcode() != null && !productRequestDto.getBarcode().isEmpty()) {
            if (productRepository.existsByBarcode(productRequestDto.getBarcode())) {
                throw new BadRequestException("Ya existe un producto con el código de barras: " + productRequestDto.getBarcode());
            }
        }

        // Validar precio positivo
        if (productRequestDto.getPrice() != null && productRequestDto.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El precio debe ser mayor a cero");
        }

        // Validar stock no negativo
        if (productRequestDto.getStock() != null && productRequestDto.getStock() < 0) {
            throw new BadRequestException("El stock no puede ser negativo");
        }

        ProductEntity productEntity = productRequestMapper.productRequestDtoToProductEntity(productRequestDto);

        // Si active no se especifica, por defecto es true
        if (productEntity.getActive() == null) {
            productEntity.setActive(true);
        }

        ProductEntity savedProduct = productRepository.save(productEntity);
        return productResponseMapper.productEntityToProductDto(savedProduct);
    }

    @Transactional
    @Override
    public ProductResponseDto update(String barcode, ProductRequestDto productRequestDto) {
        ProductEntity existingProduct = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Producto con código de barras " + barcode + " no encontrado"));

        // Validar que el nuevo código de barras no esté en uso por otro producto
        if (productRequestDto.getBarcode() != null
                && !productRequestDto.getBarcode().equals(barcode)
                && productRepository.existsByBarcode(productRequestDto.getBarcode())) {
            throw new BadRequestException("Ya existe otro producto con el código de barras: " + productRequestDto.getBarcode());
        }

        // Validar precio positivo
        if (productRequestDto.getPrice() != null && productRequestDto.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El precio debe ser mayor a cero");
        }

        // Validar stock no negativo
        if (productRequestDto.getStock() != null && productRequestDto.getStock() < 0) {
            throw new BadRequestException("El stock no puede ser negativo");
        }

        // Actualizar campos
        ProductEntity updatedEntity = productRequestMapper.productRequestDtoToProductEntity(productRequestDto);
        updatedEntity.setId(existingProduct.getId());
        updatedEntity.setCreationDate(existingProduct.getCreationDate());

        // Si active no se especifica en el DTO, mantener el valor actual
        if (updatedEntity.getActive() == null) {
            updatedEntity.setActive(existingProduct.getActive());
        }

        ProductEntity savedProduct = productRepository.save(updatedEntity);
        return productResponseMapper.productEntityToProductDto(savedProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponseDto findByBarcode(String barcode) {
        ProductEntity productEntity = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Producto con código de barras " + barcode + " no encontrado"));

        return productResponseMapper.productEntityToProductDto(productEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> searchByName(String name, Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
        List<ProductResponseDto> dtos = productPage.getContent().stream()
                .map(productResponseMapper::productEntityToProductDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, productPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDto> searchByName(String name) {
        List<ProductEntity> productEntities = productRepository.findByNameContainingIgnoreCase(name);
        return productListMapper.productEntityToProductList(productEntities);
    }

    @Transactional
    @Override
    public void delete(String barcode) {
        ProductEntity productEntity = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Producto con código de barras " + barcode + " no encontrado"));

        // Soft delete: marcar como inactivo en lugar de eliminar físicamente
        productEntity.setActive(false);
        productRepository.save(productEntity);
    }

    @Transactional
    @Override
    public ProductResponseDto updateStock(String barcode, Integer stock) {
        if (stock == null || stock < 0) {
            throw new BadRequestException("El stock debe ser un número mayor o igual a cero");
        }

        ProductEntity productEntity = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Producto con código de barras " + barcode + " no encontrado"));

        productEntity.setStock(stock);
        ProductEntity updatedProduct = productRepository.save(productEntity);

        return productResponseMapper.productEntityToProductDto(updatedProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDto> getLowStockProducts() {
        // Buscar productos activos que tienen stock menor o igual a su stock mínimo
        List<ProductEntity> lowStockProducts = productRepository.findByActiveTrue().stream()
                .filter(product -> product.getMinimumStock() != null
                        && product.getStock() <= product.getMinimumStock())
                .collect(Collectors.toList());

        return productListMapper.productEntityToProductList(lowStockProducts);
    }
}

