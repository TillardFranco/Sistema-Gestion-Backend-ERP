package com.example.farmaser.controller;

import com.example.farmaser.model.dto.stockDto.StockMovementRequestDto;
import com.example.farmaser.model.dto.stockDto.StockMovementResponseDto;
import com.example.farmaser.service.IProduct;
import com.example.farmaser.service.IStockMovement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
public class StockMovementController {

    @Autowired
    private IStockMovement stockMovementService;

    @Autowired
    private IProduct productService;

    @PostMapping("/entry")
    public ResponseEntity<StockMovementResponseDto> registerEntry(
            @Valid @RequestBody StockMovementRequestDto stockMovementRequestDto) {
        String userEmail = getCurrentUserEmail();
        StockMovementResponseDto movement = stockMovementService.registerEntry(stockMovementRequestDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @PostMapping("/exit")
    public ResponseEntity<StockMovementResponseDto> registerExit(
            @Valid @RequestBody StockMovementRequestDto stockMovementRequestDto) {
        String userEmail = getCurrentUserEmail();
        StockMovementResponseDto movement = stockMovementService.registerExit(stockMovementRequestDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @GetMapping("/movements/{productId}")
    public ResponseEntity<List<StockMovementResponseDto>> getMovementsByProduct(@PathVariable Long productId) {
        List<StockMovementResponseDto> movements = stockMovementService.getMovementsByProduct(productId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovementResponseDto>> getAllMovements() {
        List<StockMovementResponseDto> movements = stockMovementService.getAllMovements();
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<com.example.farmaser.model.dto.productDto.ProductResponseDto>> getLowStockProducts() {
        List<com.example.farmaser.model.dto.productDto.ProductResponseDto> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}


