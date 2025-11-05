package com.example.farmaser.controller;

import com.example.farmaser.config.PaginationConstants;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private IProduct productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<Page<ProductResponseDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        int validatedSize = PaginationConstants.validateAndLimitPageSize(size, PaginationConstants.MAX_PAGE_SIZE_PRODUCTS);
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(sortDir, sortBy));
        Page<ProductResponseDto> products = productService.listAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<List<ProductResponseDto>> listAllWithoutPagination() {
        List<ProductResponseDto> products = productService.listAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{barcode}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<ProductResponseDto> findByBarcode(@PathVariable String barcode) {
        ProductResponseDto product = productService.findByBarcode(barcode);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<Page<ProductResponseDto>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        int validatedSize = PaginationConstants.validateAndLimitPageSize(size, PaginationConstants.MAX_PAGE_SIZE_PRODUCTS);
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(sortDir, sortBy));
        Page<ProductResponseDto> products = productService.searchByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'CASHIER', 'VIEWER')")
    public ResponseEntity<List<ProductResponseDto>> searchByNameWithoutPagination(@RequestParam String name) {
        List<ProductResponseDto> products = productService.searchByName(name);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.save(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{barcode}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable String barcode,
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.update(barcode, productRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping("/{barcode}/stock")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<ProductResponseDto> updateStock(
            @PathVariable String barcode,
            @RequestParam Integer stock) {
        ProductResponseDto updatedProduct = productService.updateStock(barcode, stock);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{barcode}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String barcode) {
        productService.delete(barcode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE')")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts() {
        List<ProductResponseDto> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }
}


