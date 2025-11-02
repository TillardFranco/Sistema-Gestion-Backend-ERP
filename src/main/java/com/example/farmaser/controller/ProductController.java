package com.example.farmaser.controller;

import com.example.farmaser.model.dto.productDto.ProductRequestDto;
import com.example.farmaser.model.dto.productDto.ProductResponseDto;
import com.example.farmaser.service.IProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private IProduct productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<ProductResponseDto> products = productService.listAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> listAllWithoutPagination() {
        List<ProductResponseDto> products = productService.listAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<ProductResponseDto> findByBarcode(@PathVariable String barcode) {
        ProductResponseDto product = productService.findByBarcode(barcode);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<ProductResponseDto> products = productService.searchByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<ProductResponseDto>> searchByNameWithoutPagination(@RequestParam String name) {
        List<ProductResponseDto> products = productService.searchByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.save(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{barcode}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable String barcode,
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.update(barcode, productRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping("/{barcode}/stock")
    public ResponseEntity<ProductResponseDto> updateStock(
            @PathVariable String barcode,
            @RequestParam Integer stock) {
        ProductResponseDto updatedProduct = productService.updateStock(barcode, stock);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<Void> delete(@PathVariable String barcode) {
        productService.delete(barcode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts() {
        List<ProductResponseDto> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }
}


