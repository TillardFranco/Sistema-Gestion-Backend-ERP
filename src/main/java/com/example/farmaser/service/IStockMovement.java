package com.example.farmaser.service;

import com.example.farmaser.model.dto.stockDto.StockMovementRequestDto;
import com.example.farmaser.model.dto.stockDto.StockMovementResponseDto;

import java.util.List;

public interface IStockMovement {

    StockMovementResponseDto registerEntry(StockMovementRequestDto stockMovementRequestDto, String userEmail);

    StockMovementResponseDto registerExit(StockMovementRequestDto stockMovementRequestDto, String userEmail);

    List<StockMovementResponseDto> getMovementsByProduct(Long productId);

    List<StockMovementResponseDto> getAllMovements();
}


